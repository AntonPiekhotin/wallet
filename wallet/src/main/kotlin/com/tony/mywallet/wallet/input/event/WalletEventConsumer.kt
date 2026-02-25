package com.tony.mywallet.wallet.input.event

import com.tony.common.model.constant.KafkaGroup.WALLET_SERVICE
import com.tony.common.model.constant.KafkaTopic.COMPENSATION
import com.tony.common.model.constant.KafkaTopic.USER_CREATED
import com.tony.common.model.event.SagaCompensationEvent
import com.tony.common.model.event.UserCreatedEvent
import com.tony.common.saga.handler.CompensationHandler
import com.tony.common.saga.handler.SagaDispatcher
import com.tony.mywallet.common.jpa.store.SagaStore
import com.tony.mywallet.wallet.model.WalletSagaContext
import com.tony.mywallet.wallet.service.WalletService
import java.util.*
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class WalletEventConsumer(
    private val walletService: WalletService,
    handlers: List<CompensationHandler>,
    private val sagaStore: SagaStore,
) {
    private val dispatcher = SagaDispatcher(handlers)
    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [USER_CREATED],
        groupId = WALLET_SERVICE,
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun handleUserCreatedEvent(event: UserCreatedEvent, ack: Acknowledgment) {
        logger.info("Received UserCreatedEvent: $event")
        val wallet = walletService.createWallet(userId = UUID.fromString(event.userId))
        sagaStore.saveContext(event.sagaId, event.sagaOperation, WalletSagaContext.Creation(wallet.id))
        ack.acknowledge()
    }

    @KafkaListener(
        topics = [COMPENSATION],
        groupId = WALLET_SERVICE,
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
