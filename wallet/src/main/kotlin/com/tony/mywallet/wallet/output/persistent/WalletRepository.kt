package com.tony.mywallet.wallet.output.persistent

import com.tony.mywallet.wallet.model.entity.Wallet
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface WalletRepository: JpaRepository<Wallet, UUID> {
}
