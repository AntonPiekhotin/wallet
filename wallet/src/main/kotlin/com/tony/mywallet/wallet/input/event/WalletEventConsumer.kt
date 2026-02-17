package com.tony.mywallet.wallet.input.event

import com.tony.common.model.constant.KafkaConstants.Group.Companion.WALLET_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.Companion.USER_CREATED
import com.tony.common.model.event.UserCreatedEvent
import com.tony.mywallet.wallet.service.WalletService
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class WalletEventConsumer(
    private val walletService: WalletService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [USER_CREATED],
        groupId = WALLET_SERVICE,
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun handleUserCreatedEvent(event: UserCreatedEvent, ack: Acknowledgment) { // todo: compensational trans if failed?
        logger.info("Received UserCreatedEvent: $event")
        walletService.createWallet(userId = UUID.fromString(event.userId))
        ack.acknowledge()
    }
}
