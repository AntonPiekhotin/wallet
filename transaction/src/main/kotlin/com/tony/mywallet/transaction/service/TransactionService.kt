package com.tony.mywallet.transaction.service

import com.tony.common.exception.MyWalletException
import com.tony.common.model.constant.Currency
import com.tony.common.model.constant.TransactionStatus
import com.tony.common.model.constant.TransactionType
import com.tony.common.model.event.TransactionHandledEvent
import com.tony.mywallet.transaction.model.dto.DepositRequestDto
import com.tony.mywallet.transaction.model.dto.DepositResponseDto
import com.tony.mywallet.transaction.model.entity.Transaction
import com.tony.mywallet.transaction.output.event.TransactionEventProducer
import com.tony.mywallet.transaction.output.persistent.TransactionRepository
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val eventProducer: TransactionEventProducer
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getTransactionById(id: UUID): Transaction =
        transactionRepository.findById(id).orElseThrow {
            MyWalletException(404, "Transaction not found with id: {$id}")
        }

    fun initiateDeposit(request: DepositRequestDto): Mono<DepositResponseDto> = with(request) {
        val transaction = Transaction(
            sagaId = UUID.randomUUID().toString(),
            type = TransactionType.DEPOSIT,
            status = TransactionStatus.CREATED,
            amount = amount,
            currency = Currency.valueOf(currency),
            targetWalletId = walletId
        )
        eventProducer.sendDepositInitiatedEvent(transaction, request)
        transactionRepository.save(transaction).also {
            logger.info("Deposit initiated for wallet: $walletId")
        }
        return DepositResponseDto(transaction.id.toString()).toMono()
    }

    fun handleTransaction(event: TransactionHandledEvent) {
        val transaction = getTransactionById(event.transactionId)
        when (event.status) {
            TransactionStatus.COMPLETED -> {
                transaction.status = TransactionStatus.COMPLETED
                if (event is TransactionHandledEvent.Deposit) transaction.redirectUrl = event.redirectUrl
            }
            else -> transaction.status = event.status
        }
        transactionRepository.save(transaction)
            .also { logger.info("Transaction ${transaction.id} completed for wallet: ${transaction.targetWalletId}") }
    }
}
