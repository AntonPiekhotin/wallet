package com.tony.auth.handler

import com.tony.auth.service.KeycloakService
import com.tony.common.saga.handler.CompensationHandler
import com.tony.common.model.constant.SagaContextKeys.USER_ID
import com.tony.common.model.constant.SagaOperation
import com.tony.common.model.event.SagaCompensationEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AuthRegistrationCompensationHandler(
    private val keycloakService: KeycloakService
) : CompensationHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override val supportedSagaOperation: SagaOperation = SagaOperation.USER_CREATED

    override fun handle(event: SagaCompensationEvent) {
        val userId = event.traceability[USER_ID]
        if (userId != null) {
            keycloakService.deleteUser(userId)
        } else {
            logger.info("User not found with id $userId")
        }
    }
}
