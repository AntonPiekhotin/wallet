package com.tony.mywallet.user.input.event

import com.tony.common.model.constant.KafkaConstants.Group.Companion.USER_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.Companion.USER_CREATED
import com.tony.common.model.event.UserCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class UserEventConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [USER_CREATED],
        groupId = USER_SERVICE,
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun handleUserCreatedEvent(event: UserCreatedEvent, acknowledgment: Acknowledgment) {
        // TODO: Handle the event, e.g., create a user profile in the database
        logger.info("Received UserCreatedEvent: $event")
        acknowledgment.acknowledge()
    }
}
