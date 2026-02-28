package com.tony.common.model.event

import com.tony.common.model.constant.KafkaTopic.DEPOSIT_CAPTURED
import com.tony.common.model.constant.KafkaTopic.DEPOSIT_INITIATED
import com.tony.common.model.constant.KafkaTopic.P2P_TRANSFER
import com.tony.common.model.constant.KafkaTopic.WITHDRAWAL
import com.tony.common.model.constant.SagaOperation
import java.math.BigDecimal
import java.util.UUID

sealed class TransactionInitiatedEvent : SagaEvent() {

    data class Deposit(
        override val sagaId: String,
        val transactionId: UUID,
        val targetWalletId: UUID,
        val amount: BigDecimal,
        override val traceability: MutableMap<String, String>,
    ) : TransactionInitiatedEvent() {
        override val topic = DEPOSIT_INITIATED
        override val sagaOperation = SagaOperation.DEPOSIT
    }

    data class DepositCapturedEvent(
        override val sagaId: String,
        override val traceability: MutableMap<String, String>,
        val transactionId: UUID,
        val status: Status,
        val redirectUrl: String? = null,
    ) : SagaEvent() {
        override val topic = DEPOSIT_CAPTURED
        override val sagaOperation = SagaOperation.DEPOSIT

        enum class Status { SUCCESS, FAILED }
    }

    data class Withdrawal(
        override val sagaId: String,
        val transactionId: UUID,
        val sourceWalletId: UUID,
        val amount: BigDecimal,
        val destinationIban: String,
        override val traceability: MutableMap<String, String>,
    ) : TransactionInitiatedEvent() {
        override val topic = WITHDRAWAL
        override val sagaOperation = SagaOperation.WITHDRAWAL
    }

    data class P2PTransfer(
        override val sagaId: String,
        val transactionId: UUID,
        val sourceWalletId: UUID,
        val targetWalletId: UUID,
        val amount: BigDecimal,
        override val traceability: MutableMap<String, String>,
    ) : TransactionInitiatedEvent() {
        override val topic = P2P_TRANSFER
        override val sagaOperation = SagaOperation.P2P_TRANSFER
    }
}