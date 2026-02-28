package com.tony.mywallet.payment.gateway.input.event

import com.tony.common.model.constant.KafkaGroup.PAYMENT_GATEWAY_SERVICE
import com.tony.common.model.constant.KafkaTopic.DEPOSIT_INITIATED
import com.tony.common.model.event.TransactionInitiatedEvent
import com.tony.mywallet.payment.gateway.output.event.PaymentGatewayEventProducer
import java.lang.Thread.sleep
import java.math.BigDecimal
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

typealias DepositStatus = TransactionInitiatedEvent.DepositCapturedEvent.Status

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
        sleep(3000)
        when {
            amount == BigDecimal.valueOf(13) ->
                throw RuntimeException("Simulating error")
            amount < BigDecimal.TEN ->
                eventProducer.sendEvent(
                    TransactionInitiatedEvent.DepositCapturedEvent(
                        sagaId = sagaId,
                        traceability = traceability,
                        transactionId = transactionId,
                        status = DepositStatus.FAILED
                    )
                )
            amount >= BigDecimal.TEN ->
                eventProducer.sendEvent(
                    TransactionInitiatedEvent.DepositCapturedEvent(
                        sagaId = sagaId,
                        traceability = traceability,
                        transactionId = transactionId,
                        status = DepositStatus.SUCCESS,
                        redirectUrl = "https://www.google.com"
                    )
                )
        }
        ack.acknowledge()
    }
}
