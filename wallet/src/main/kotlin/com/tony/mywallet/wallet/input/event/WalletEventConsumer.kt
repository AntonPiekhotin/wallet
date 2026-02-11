package com.tony.mywallet.wallet.input.event

import com.tony.common.model.constant.KafkaConstants.Group.Companion.WALLET_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.Companion.USER_CREATED
import com.tony.common.model.event.UserCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class WalletEventConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = [USER_CREATED], groupId = WALLET_SERVICE, containerFactory = "kafkaListenerContainerFactory")
    fun handleUserCreatedEvent(event: UserCreatedEvent, acknowledgment: Acknowledgment) {
        // TODO: Handle the event, e.g., create a wallet for the new user
        logger.info("Received UserCreatedEvent: $event")
    }
}