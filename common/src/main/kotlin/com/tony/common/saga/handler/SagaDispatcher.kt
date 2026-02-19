package com.tony.common.saga.handler

import com.tony.common.model.event.SagaCompensationEvent

class SagaDispatcher(
    handlers: List<CompensationHandler>
) {
    private val handlerMap = handlers.associateBy { it.supportedSagaOperation }

    fun dispatch(event: SagaCompensationEvent) {
        val handler = handlerMap[event.sagaOperation]
        handler?.handle(event)
    }
}