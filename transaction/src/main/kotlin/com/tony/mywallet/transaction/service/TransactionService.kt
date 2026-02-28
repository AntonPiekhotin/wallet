package com.tony.mywallet.transaction.service

import com.tony.common.exception.MyWalletException
import com.tony.common.model.constant.Currency
import com.tony.common.model.event.TransactionInitiatedEvent
import com.tony.mywallet.transaction.model.dto.DepositRequestDto
import com.tony.mywallet.transaction.model.dto.DepositResponseDto
import com.tony.mywallet.transaction.model.entity.Transaction
import com.tony.mywallet.transaction.model.entity.TransactionStatus
import com.tony.mywallet.transaction.model.entity.TransactionType
import com.tony.mywallet.transaction.output.event.TransactionEventProducer
import com.tony.mywallet.transaction.output.persistent.TransactionRepository
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

typealias DepositStatus = TransactionInitiatedEvent.DepositCapturedEvent.Status

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

    fun deposit(request: DepositRequestDto): Mono<DepositResponseDto> {
        val transaction = Transaction(
            sagaId = UUID.randomUUID().toString(),
            type = TransactionType.DEPOSIT,
            status = TransactionStatus.PENDING,
            amount = request.amount,
            currency = Currency.valueOf(request.currency),
            targetWalletId = request.walletId
        )
        eventProducer.sendEvent(
            TransactionInitiatedEvent.Deposit(
                sagaId = transaction.sagaId,
                transactionId = transaction.id,
                targetWalletId = request.walletId,
                amount = request.amount,
                traceability = mutableMapOf()
            )
        )
        transactionRepository.save(transaction).also {
            logger.info("Deposit initiated for wallet: ${request.walletId}")
        }
        return DepositResponseDto(transaction.id.toString()).toMono()
    }

    fun handleDepositCaptured(event: TransactionInitiatedEvent.DepositCapturedEvent) {
        val transaction = getTransactionById(event.transactionId)
        when (event.status) {
            DepositStatus.SUCCESS -> {
                transaction.status = TransactionStatus.REDIRECT
                transaction.redirectUrl = event.redirectUrl
            }

            DepositStatus.FAILED ->
                transaction.status = TransactionStatus.FAILED
        }
        transactionRepository.save(transaction)
            .also { logger.info("Deposit captured for wallet: ${transaction.targetWalletId}") }
    }

}
