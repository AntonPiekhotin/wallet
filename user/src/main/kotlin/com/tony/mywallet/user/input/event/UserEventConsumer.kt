package com.tony.mywallet.user.input.event

import com.tony.common.model.constant.KafkaConstants.Group.USER_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.USER_CREATED
import com.tony.common.model.event.SagaCompensationEvent
import com.tony.common.model.event.UserCreatedEvent
import com.tony.mywallet.user.mapper.UserMapper.toUserEntity
import com.tony.mywallet.user.output.event.UserEventProducer
import com.tony.mywallet.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class UserEventConsumer(
    private val userService: UserService,
    private val userEventProducer: UserEventProducer
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [USER_CREATED],
        groupId = USER_SERVICE,
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun handleUserCreatedEvent(
        event: UserCreatedEvent,
        acknowledgment: Acknowledgment
    ) {
        logger.info("Received UserCreatedEvent: $event")
        try {
            userService.createUser(event.toUserEntity())
            //todo: save saga id
        } catch (e: Exception) {
            logger.error("Error while creating user ", e)
            userEventProducer.sendEvent(
                SagaCompensationEvent(
                    sagaId = event.sagaId,
                    traceability = event.traceability,
                    reason = "Exception during user creation: " + (e.message ?: "Unknown error"),
                    sourceService = this.javaClass.simpleName
                )
            )
        } finally {
            acknowledgment.acknowledge()
        }
    }
}
