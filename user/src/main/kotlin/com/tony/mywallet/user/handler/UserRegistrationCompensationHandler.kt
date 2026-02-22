package com.tony.mywallet.user.handler

import com.tony.common.model.event.SagaCompensationEvent
import com.tony.mywallet.common.jpa.handler.AbstractCompensationHandler
import com.tony.mywallet.common.jpa.store.SagaStore
import com.tony.mywallet.user.output.persistent.UserRepository
import com.tony.mywallet.user.util.UserSagaBindings
import com.tony.mywallet.user.util.UserSagaContext
import org.springframework.stereotype.Component

@Component
class UserRegistrationCompensationHandler(
    override val sagaStore: SagaStore,
    private val userRepository: UserRepository,
) : AbstractCompensationHandler<UserSagaContext.Creation>(
    UserSagaBindings.USER_CREATION
) {

    override fun compensate(event: SagaCompensationEvent, context: UserSagaContext.Creation) {
        logger.info("Rolling back user creation for userId: ${context.userId}")
        userRepository.deleteById(context.userId).also {
            logger.info("User with id: ${context.userId} has been deleted.")
        }
    }
}
