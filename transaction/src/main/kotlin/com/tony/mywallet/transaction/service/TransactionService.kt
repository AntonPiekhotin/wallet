package com.tony.mywallet.transaction.service

import com.tony.common.exception.MyWalletException
import com.tony.mywallet.transaction.output.persistent.TransactionRepository
import java.util.*
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {

    fun getTransactionById(id: String) =
        transactionRepository.findById(UUID.fromString(id)).orElseThrow {
            MyWalletException(404, "Transaction not found with id: {$id}")
        }
}
