package com.tony.mywallet.transaction.output.event

import com.tony.common.model.event.KafkaEvent
import com.tony.common.model.event.TransactionInitiatedEvent
import com.tony.mywallet.transaction.model.dto.DepositRequestDto
import com.tony.mywallet.transaction.model.entity.Transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class TransactionEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun sendDepositInitiatedEvent(transaction: Transaction, request: DepositRequestDto) =
        sendEvent(
            TransactionInitiatedEvent.Deposit(
                sagaId = transaction.sagaId,
                transactionId = transaction.id,
                targetWalletId = request.walletId,
                amount = request.amount
            )
        )

    private fun sendEvent(event: KafkaEvent) {
        kafkaTemplate.send(
            event.topic,
            event.key,
            event
        ).also {
            logger.info("Sent event to topic ${event.topic} with key ${event.key}: $event")
        }
    }
}