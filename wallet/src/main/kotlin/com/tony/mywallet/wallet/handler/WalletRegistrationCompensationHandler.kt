package com.tony.mywallet.wallet.handler

import com.tony.common.model.event.SagaCompensationEvent
import com.tony.mywallet.common.jpa.handler.AbstractCompensationHandler
import com.tony.mywallet.common.jpa.store.SagaStore
import com.tony.mywallet.wallet.model.WalletSagaContext
import com.tony.mywallet.wallet.output.persistent.WalletRepository
import com.tony.mywallet.wallet.util.WalletSagaBindings
import org.springframework.stereotype.Component

@Component
class WalletRegistrationCompensationHandler(
    override val sagaStore: SagaStore,
    private val walletRepository: WalletRepository,
) : AbstractCompensationHandler<WalletSagaContext.Creation>(
    WalletSagaBindings.USER_CREATION
) {

    override fun compensate(event: SagaCompensationEvent, context: WalletSagaContext.Creation) {
        logger.info("Rolling back wallet creation for walletId: ${context.walletId}")
        walletRepository.deleteById(context.walletId).also {
            logger.info(
                "Deleted wallet with id: {}",
                context.walletId
            )
        }
    }
}
