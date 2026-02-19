package com.tony.common.saga.handler

import com.tony.common.model.constant.SagaOperation
import com.tony.common.model.event.SagaCompensationEvent

interface CompensationHandler {
    val supportedSagaOperation: SagaOperation

    fun handle(event: SagaCompensationEvent)
}

