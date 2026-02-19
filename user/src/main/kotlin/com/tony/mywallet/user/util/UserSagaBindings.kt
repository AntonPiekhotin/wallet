package com.tony.mywallet.user.util

import com.tony.common.model.constant.SagaOperation
import com.tony.mywallet.common.jpa.handler.SagaBinding

object UserSagaBindings {

    val USER_CREATION = SagaBinding(
        SagaOperation.USER_CREATED,
        UserSagaContext.Creation::class.java
    )
}