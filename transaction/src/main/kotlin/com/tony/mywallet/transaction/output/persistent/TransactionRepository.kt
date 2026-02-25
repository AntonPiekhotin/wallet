package com.tony.mywallet.transaction.output.persistent

import com.tony.mywallet.transaction.model.entity.Transaction
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: JpaRepository<Transaction, UUID> {
}
