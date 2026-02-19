package com.tony.mywallet.common.jpa.handler

import com.tony.common.model.constant.SagaOperation
import com.tony.mywallet.common.jpa.repo.SagaPayload

data class SagaBinding<T : SagaPayload>(
    val operation: SagaOperation,
    val payloadClass: Class<T>
)
