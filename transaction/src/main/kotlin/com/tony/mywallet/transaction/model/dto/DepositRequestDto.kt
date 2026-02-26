package com.tony.mywallet.transaction.model.dto

import java.math.BigDecimal
import java.util.UUID

data class DepositRequestDto(
    val amount: BigDecimal,
    val currency: String,
    val walletId: UUID
)