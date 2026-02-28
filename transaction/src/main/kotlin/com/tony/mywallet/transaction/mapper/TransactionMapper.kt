package com.tony.mywallet.transaction.mapper

import com.tony.mywallet.transaction.model.dto.TransactionResponseDto
import com.tony.mywallet.transaction.model.entity.Transaction

object TransactionMapper {

    fun Transaction.toResponseDto() =
        TransactionResponseDto(
            id = id,
            type = type,
            status = status,
            amount = amount,
            currency = currency,
            sourceWalletId = sourceWalletId,
            targetWalletId = targetWalletId,
            redirectUrl = redirectUrl,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
}
