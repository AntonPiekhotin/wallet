package com.tony.mywallet.payment.gateway.input.event

import com.tony.common.model.constant.KafkaGroup.PAYMENT_GATEWAY_SERVICE
import com.tony.common.model.constant.KafkaTopic.DEPOSIT_INITIATED
import com.tony.common.model.constant.TransactionStatus
import com.tony.common.model.event.TransactionHandledEvent
import com.tony.common.model.event.TransactionInitiatedEvent
import com.tony.mywallet.payment.gateway.output.event.PaymentGatewayEventProducer
import java.lang.Thread.sleep
import java.math.BigDecimal
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class PaymentGatewayEventConsumer(
    private val eventProducer: PaymentGatewayEventProducer
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = [DEPOSIT_INITIATED],
        groupId = PAYMENT_GATEWAY_SERVICE,
        containerFactory = "kafkaListenerContainerFactory",
    )
    fun onDeposit(event: TransactionInitiatedEvent.Deposit, ack: Acknowledgment) = with(event) {
        logger.info("Received deposit event: $this")
        ack.acknowledge()
        sleep(3000)
        when {
            amount == BigDecimal.valueOf(13) -> throw RuntimeException("Simulating error")
            amount >= BigDecimal.TEN -> handleSuccessfulDeposit()
            amount < BigDecimal.TEN -> handleFailedDeposit()
        }
    }

    private fun TransactionInitiatedEvent.Deposit.handleSuccessfulDeposit() {
        eventProducer.sendEvent(
            TransactionHandledEvent.Deposit(
                sagaId = sagaId,
                traceability = traceability,
                transactionId = transactionId,
                status = TransactionStatus.COMPLETED,
                redirectUrl = "https://www.google.com",
                targetWalletId = targetWalletId,
                amount = amount
            )
        )
    }

    private fun TransactionInitiatedEvent.Deposit.handleFailedDeposit() {
        eventProducer.sendEvent(
            TransactionHandledEvent.Deposit(
                sagaId = sagaId,
                traceability = traceability,
                transactionId = transactionId,
                status = TransactionStatus.FAILED,
                targetWalletId = targetWalletId,
                amount = amount
            )
        )
    }
}
