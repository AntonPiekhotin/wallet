package com.tony.mywallet.wallet.service

import com.tony.common.exception.MyWalletException
import com.tony.common.model.constant.TransactionStatus
import com.tony.common.model.constant.TransactionType
import com.tony.common.model.event.TransactionHandledEvent
import com.tony.mywallet.wallet.model.entity.Wallet
import com.tony.mywallet.wallet.output.persistent.WalletRepository
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WalletService(
    private val walletRepository: WalletRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun createWallet(userId: UUID): Wallet {
        logger.debug("Creating wallet for user {}", userId)
        val wallet = Wallet(userId = userId)
        return walletRepository.save(wallet).also { logger.info("Created wallet for user $userId") }
    }

    fun handleTransaction(event: TransactionHandledEvent) {
        logger.info("Handling transaction event: {}", event)
        when (event.transactionType) {
            TransactionType.DEPOSIT -> handleDepositEvent(event)
            TransactionType.WITHDRAWAL -> handleWithdrawalEvent(event)
        }
    }

    private fun handleDepositEvent(event: TransactionHandledEvent) {
        logger.info("Handling deposit transaction")
        if (event !is TransactionHandledEvent.Deposit) {
            return
        }
        when (event.status) {
            TransactionStatus.COMPLETED -> handleCompletedDepositEvent(event)
            TransactionStatus.FAILED -> return
            TransactionStatus.PENDING -> {} //TODO handle pending status
            TransactionStatus.CREATED -> {} //TODO handle created status
            TransactionStatus.REDIRECT -> {} //TODO handle redirect status
        }
    }

    private fun handleCompletedDepositEvent(event: TransactionHandledEvent.Deposit) {
        val targetWallet = getWalletById(event.targetWalletId)
        targetWallet.accounts[0].balance += event.amount

        walletRepository.save(targetWallet).also {
            logger.info("Deposited ${event.amount} to wallet ${targetWallet.id}")
        }
        //todo: handle multiple accounts, currency, etc...
    }

    private fun handleWithdrawalEvent(event: TransactionHandledEvent) {
        //TODO
    }

    private fun getWalletById(walletId: UUID): Wallet =
        walletRepository.findById(walletId).orElseThrow { MyWalletException(404, "Wallet not found") }

}
