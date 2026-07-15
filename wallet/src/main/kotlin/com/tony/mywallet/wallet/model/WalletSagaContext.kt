package com.tony.mywallet.wallet.model

import com.tony.common.model.constant.TransactionStatus
import com.tony.common.model.constant.TransactionType
import com.tony.mywallet.common.jpa.repo.SagaPayload
import java.math.BigDecimal
import java.util.UUID

sealed class WalletSagaContext : SagaPayload {

    data class UserCreation(
        val walletId: UUID
    ) : WalletSagaContext()

    data class TransactionHandled(
        val sourceWalletId: UUID? = null,
        val targetWalletId: UUID? = null,
        val transactionId: UUID,
        val status: TransactionStatus,
        val amount: BigDecimal,
        val type: TransactionType,
    ) : WalletSagaContext()
}
