package com.tony.mywallet.wallet.service

import com.tony.mywallet.wallet.model.entity.Wallet
import com.tony.mywallet.wallet.output.persistent.WalletRepository
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WalletService(
    private val walletRepository: WalletRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createWallet(userId: UUID): Wallet {
        logger.debug("Creating wallet for user {}", userId)
        val wallet = Wallet(userId = userId)
        return walletRepository.save(wallet).also { logger.info("Created wallet for user $userId") }
    }
}

