package com.tony.mywallet.user.input.event

import com.tony.common.saga.handler.CompensationHandler
import com.tony.common.saga.handler.SagaDispatcher
import com.tony.common.model.constant.KafkaConstants.Group.USER_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.COMPENSATION
import com.tony.common.model.constant.KafkaConstants.Topic.USER_CREATED
import com.tony.common.model.constant.Source.USER_SOURCE
import com.tony.common.model.event.SagaCompensationEvent
import com.tony.common.model.event.UserCreatedEvent
import com.tony.mywallet.common.jpa.store.SagaStore
import com.tony.mywallet.user.mapper.UserMapper.toUserEntity
import com.tony.mywallet.user.output.event.UserEventProducer
import com.tony.mywallet.user.service.UserService
import com.tony.mywallet.user.util.UserSagaContext
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class UserEventConsumer(
    private val userService: UserService,
    private val userEventProducer: UserEventProducer,
    handlers: List<CompensationHandler>,
    private val sagaStore: SagaStore
) {
    private val dispatcher = SagaDispatcher(handlers)
    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [USER_CREATED],
        groupId = USER_SERVICE,
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun handleUserCreatedEvent(
        event: UserCreatedEvent,
        acknowledgment: Acknowledgment
    ) = with(event) {
        logger.info("Received UserCreatedEvent: $event")
        try {
            val user = userService.createUser(toUserEntity())
            sagaStore.saveContext(sagaId, sagaOperation, UserSagaContext.Creation(user.id))
        } catch (e: Exception) {
            logger.error("Error while creating user ", e)
            userEventProducer.sendEvent(
                SagaCompensationEvent(
                    sagaId = sagaId,
                    traceability = traceability,
                    reason = "Exception during user creation: " + (e.message ?: "Unknown error"),
                    sourceService = USER_SOURCE,
                    sagaOperation = sagaOperation,
                )
            )
        } finally {
            acknowledgment.acknowledge()
        }
    }

    @KafkaListener(
        topics = [COMPENSATION],
        groupId = USER_SERVICE,
        containerFactory = "kafkaListenerContainerFactory",
    )
    fun onCompensation(event: SagaCompensationEvent, acknowledgment: Acknowledgment) {
        logger.debug("Received compensation event: {}", event)
        try {
            dispatcher.dispatch(event)
        } finally {
            acknowledgment.acknowledge()
        }
    }
}
