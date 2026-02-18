package com.tony.mywallet.user.handler

import com.tony.common.handler.CompensationHandler
import com.tony.common.model.constant.SagaConstants.SagaOperation
import com.tony.common.model.event.SagaCompensationEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UserRegistrationCompensationHandler : CompensationHandler {
    override val supportedSagaOperation: SagaOperation = SagaOperation.USER_CREATED

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(event: SagaCompensationEvent) {
        //todo: implement
    }
}
