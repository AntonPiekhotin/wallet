package com.tony.auth.input.event

import com.tony.common.handler.CompensationHandler
import com.tony.common.handler.SagaDispatcher
import com.tony.common.model.constant.KafkaConstants.Group.AUTH_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.COMPENSATION
import com.tony.common.model.event.SagaCompensationEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class AuthEventConsumer(
    handlers: List<CompensationHandler>
) {
    private val dispatcher = SagaDispatcher(handlers)
    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [COMPENSATION],
        groupId = AUTH_SERVICE,
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
