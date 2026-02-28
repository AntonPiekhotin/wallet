package com.tony.mywallet.transaction.model.dto

import com.tony.common.model.constant.Currency
import com.tony.mywallet.transaction.model.entity.TransactionStatus
import com.tony.mywallet.transaction.model.entity.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionResponseDto(
    val id: UUID,
    val type: TransactionType,
    var status: TransactionStatus,
    val amount: BigDecimal,
    val currency: Currency,
    val sourceWalletId: UUID? = null,
    val targetWalletId: UUID? = null,
    var redirectUrl: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)
