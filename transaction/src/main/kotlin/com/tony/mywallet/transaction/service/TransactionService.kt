package com.tony.mywallet.transaction.service

import com.tony.common.exception.MyWalletException
import com.tony.common.model.event.TransactionInitiatedEvent
import com.tony.mywallet.transaction.model.dto.DepositRequestDto
import com.tony.mywallet.transaction.model.dto.DepositResponseDto
import com.tony.mywallet.transaction.model.entity.Transaction
import com.tony.mywallet.transaction.model.entity.TransactionStatus
import com.tony.mywallet.transaction.model.entity.TransactionType
import com.tony.mywallet.transaction.output.event.TransactionEventProducer
import com.tony.mywallet.transaction.output.persistent.TransactionRepository
import java.util.*
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val eventProducer: TransactionEventProducer
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun getTransactionById(id: String) =
        transactionRepository.findById(UUID.fromString(id)).orElseThrow {
            MyWalletException(404, "Transaction not found with id: {$id}")
        }

    fun deposit(request: DepositRequestDto): Mono<DepositResponseDto> {
        val transaction = Transaction(
            sagaId = UUID.randomUUID().toString(),
            type = TransactionType.DEPOSIT,
            status = TransactionStatus.PENDING,
            amount = request.amount,
            currency = request.currency,
            targetWalletId = request.walletId
        )
        eventProducer.sendEvent(
            TransactionInitiatedEvent.Deposit(
                sagaId = transaction.sagaId,
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

}
