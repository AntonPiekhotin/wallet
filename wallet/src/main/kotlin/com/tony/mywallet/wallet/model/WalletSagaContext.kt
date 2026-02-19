package com.tony.mywallet.wallet.model

import com.tony.mywallet.common.jpa.repo.SagaPayload
import java.util.UUID

sealed class WalletSagaContext : SagaPayload {

    data class Creation(
        val walletId: UUID
    ) : WalletSagaContext()

}
