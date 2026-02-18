package com.tony.common.handler

import com.tony.common.model.constant.SagaConstants.SagaOperation
import com.tony.common.model.event.SagaCompensationEvent

interface CompensationHandler {
    val supportedSagaOperation: SagaOperation

    fun handle(event: SagaCompensationEvent)
}

