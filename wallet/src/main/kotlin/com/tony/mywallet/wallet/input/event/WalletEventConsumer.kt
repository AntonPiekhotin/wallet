package com.tony.mywallet.wallet.input.event

import com.tony.common.exception.MyWalletException
import com.tony.common.handler.CompensationHandler
import com.tony.common.handler.SagaDispatcher
import com.tony.common.model.constant.KafkaConstants.Group.WALLET_SERVICE
import com.tony.common.model.constant.KafkaConstants.Topic.COMPENSATION
import com.tony.common.model.constant.KafkaConstants.Topic.USER_CREATED
import com.tony.common.model.constant.SagaConstants.Source.WALLET_SOURCE
import com.tony.common.model.event.SagaCompensationEvent
import com.tony.common.model.event.UserCreatedEvent
import com.tony.mywallet.wallet.output.event.WalletEventProducer
import com.tony.mywallet.wallet.service.WalletService
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class WalletEventConsumer(
    private val walletService: WalletService,
    private val walletEventProducer: WalletEventProducer,
    handlers: List<CompensationHandler>
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
        try {
            walletService.createWallet(userId = UUID.fromString(event.userId))
            //todo: save saga id
        } catch (e: Exception) {
            logger.error("Error while creating wallet ", e)
            walletEventProducer.sendEvent(
                SagaCompensationEvent(
                    sagaId = event.sagaId,
                    traceability = event.traceability,
                    reason = "Exception during wallet creation: " + (e.message ?: "Unknown error"),
                    sourceService = WALLET_SOURCE,
                    sagaOperation = event.sagaOperation,
                )
            )
            throw MyWalletException(500, e.message, e)
        } finally {
            ack.acknowledge()
        }
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
