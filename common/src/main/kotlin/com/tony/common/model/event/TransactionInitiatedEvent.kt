package com.tony.common.model.event

import com.tony.common.model.constant.KafkaTopic.TRANSACTION_INITIATED
import com.tony.common.model.constant.SagaOperation
import java.math.BigDecimal
import java.util.UUID

sealed class TransactionInitiatedEvent(
    override val topic: String = TRANSACTION_INITIATED
) : SagaEvent() {

    data class Deposit(
        override val sagaId: String,
        val transactionId: UUID,
        val targetWalletId: UUID,
        val amount: BigDecimal,
        override val traceability: MutableMap<String, String>? = mutableMapOf(),
    ) : TransactionInitiatedEvent() {
        override val sagaOperation = SagaOperation.DEPOSIT
    }

    data class Withdrawal(
        override val sagaId: String,
        val transactionId: UUID,
        val sourceWalletId: UUID,
        val amount: BigDecimal,
        val destinationIban: String,
        override val traceability: MutableMap<String, String>? = mutableMapOf(),
    ) : TransactionInitiatedEvent() {
        override val sagaOperation = SagaOperation.WITHDRAWAL
    }

    data class P2PTransfer(
        override val sagaId: String,
        val transactionId: UUID,
        val sourceWalletId: UUID,
        val targetWalletId: UUID,
        val amount: BigDecimal,
        override val traceability: MutableMap<String, String>? = mutableMapOf(),
    ) : TransactionInitiatedEvent() {
        override val sagaOperation = SagaOperation.P2P_TRANSFER
    }
}