package com.tony.mywallet.notification.input.event

import com.tony.common.model.constant.KafkaConstants.Group.Companion.NOTIFICATION_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.Companion.USER_CREATED
import com.tony.common.model.event.UserCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class NotificationEventConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = [USER_CREATED], groupId = NOTIFICATION_SERVICE, containerFactory = "kafkaListenerContainerFactory")
    fun handleUserCreatedEvent(event: UserCreatedEvent, acknowledgment: Acknowledgment) {
        // TODO: Handle the user created event, e.g., send a welcome email
        logger.info("Received UserCreatedEvent: $event")
        acknowledgment.acknowledge()
    }
}