package com.tony.common.handler

import com.tony.common.model.constant.SagaConstants.SagaType
import com.tony.common.model.event.SagaCompensationEvent

interface CompensationHandler {
    val supportedSagaType: SagaType

    fun handle(event: SagaCompensationEvent)
}

