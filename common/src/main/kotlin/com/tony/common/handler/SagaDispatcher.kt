package com.tony.common.handler

import com.tony.common.model.event.SagaCompensationEvent

class SagaDispatcher(
    handlers: List<CompensationHandler>
) {
    private val handlerMap = handlers.associateBy { it.supportedSagaType }

    fun dispatch(event: SagaCompensationEvent) {
        val handler = handlerMap[event.sagaType]
        handler?.handle(event)
    }
}