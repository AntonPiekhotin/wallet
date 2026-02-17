package com.tony.mywallet.wallet.model

import com.tony.common.model.constant.Currency
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.UpdateTimestamp

@Entity
data class Account(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    val wallet: Wallet,

    @Column(nullable = false, length = 3)
    val currency: Currency,

    @Column(nullable = false)
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var lockedBalance: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var isDefault: Boolean = false,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    val updatedAt: LocalDateTime
)
