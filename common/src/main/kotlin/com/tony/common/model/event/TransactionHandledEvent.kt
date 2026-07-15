package com.tony.common.model.event

import com.tony.common.model.constant.KafkaTopic.TRANSACTION_HANDLED
import com.tony.common.model.constant.SagaOperation
import com.tony.common.model.constant.TransactionStatus
import com.tony.common.model.constant.TransactionType
import java.math.BigDecimal
import java.util.UUID

sealed class TransactionHandledEvent(
    override val sagaId: String,
    override val traceability: MutableMap<String, String>?,
    override val topic: String = TRANSACTION_HANDLED,
    open val transactionId: UUID,
    open val sourceWalletId: UUID? = null,
    open val targetWalletId: UUID? = null,
    open val status: TransactionStatus,
    open val amount: BigDecimal,
    open val transactionType: TransactionType,
) : SagaEvent() {

    data class Deposit(
        override val sagaId: String,
        override val transactionId: UUID,
        override val targetWalletId: UUID,
        override val status: TransactionStatus,
        override val traceability: MutableMap<String, String>? = null,
        override val amount: BigDecimal,
        override val transactionType: TransactionType = TransactionType.DEPOSIT,
        val redirectUrl: String? = null,
    ) : TransactionHandledEvent(
        sagaId,
        traceability,
        transactionId = transactionId,
        status = status,
        amount = amount,
        transactionType = transactionType,
        targetWalletId = targetWalletId
    ) {
        override val sagaOperation = SagaOperation.DEPOSIT
    }
}
