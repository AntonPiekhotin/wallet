package com.tony.mywallet.common.jpa.handler

import com.tony.common.model.event.SagaCompensationEvent
import com.tony.common.saga.handler.CompensationHandler
import com.tony.mywallet.common.jpa.repo.SagaPayload
import com.tony.mywallet.common.jpa.store.SagaStore
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractCompensationHandler<T : SagaPayload>(
    private val binding: SagaBinding<T>,
) : CompensationHandler {

    abstract val sagaStore: SagaStore

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)

    final override val supportedSagaOperation = binding.operation

    final override fun handle(event: SagaCompensationEvent) {
        val context = sagaStore.getContext(event.sagaId, binding.payloadClass)
        if (context == null) {
            logger.warn("Saga [${event.sagaId}]: No context of type ${binding.payloadClass.simpleName} found, skipping rollback.")
            return
        }
        compensate(event, context)
        logger.debug("Saga [${event.sagaId}]: Compensation finished. Cleaning up journal.")
        sagaStore.deleteContext(event.sagaId)
    }

    protected abstract fun compensate(event: SagaCompensationEvent, context: T)
}