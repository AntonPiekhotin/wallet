package com.tony.mywallet.transaction.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
data class Transaction(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val sagaId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType,

    @Column(nullable = false)
    val status: TransactionStatus,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val currency: String,

    val sourceWalletId: UUID? = null,

    val targetWalletId: UUID? = null,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
)

enum class TransactionType {
    DEPOSIT, WITHDRAWAL
}

enum class TransactionStatus {
    PENDING, COMPLETED, FAILED
}
