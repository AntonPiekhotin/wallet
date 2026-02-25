package com.tony.common.model.event

import com.tony.common.model.constant.SagaOperation
import com.tony.common.model.constant.KafkaTopic.DEPOSIT
import com.tony.common.model.constant.KafkaTopic.P2P_TRANSFER
import com.tony.common.model.constant.KafkaTopic.WITHDRAWAL
import java.math.BigDecimal
import java.util.UUID

sealed class TransactionInitiatedEvent: SagaEvent() {

    data class Deposit(
        override val sagaId: String,
        val targetWalletId: UUID,
        val amount: BigDecimal,
        override val traceability: MutableMap<String, String>,
    ) : TransactionInitiatedEvent() {
        override val topic = DEPOSIT
        override val sagaOperation = SagaOperation.DEPOSIT
    }

    data class Withdrawal(
        override val sagaId: String,
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
        val sourceWalletId: UUID,
        val targetWalletId: UUID,
        val amount: BigDecimal,
        override val traceability: MutableMap<String, String>,
    ) : TransactionInitiatedEvent() {
        override val topic = P2P_TRANSFER
        override val sagaOperation = SagaOperation.P2P_TRANSFER
    }
}