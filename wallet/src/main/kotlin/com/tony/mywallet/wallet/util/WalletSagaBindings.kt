package com.tony.mywallet.wallet.util

import com.tony.common.model.constant.SagaOperation
import com.tony.mywallet.common.jpa.handler.SagaBinding
import com.tony.mywallet.wallet.model.WalletSagaContext

object WalletSagaBindings {

    val USER_CREATION = SagaBinding(
        SagaOperation.USER_CREATED,
        WalletSagaContext.Creation::class.java
    )
}