package com.tony.mywallet.transaction.input.web

import com.tony.mywallet.transaction.model.dto.DepositRequestDto
import com.tony.mywallet.transaction.model.dto.DepositResponseDto
import com.tony.mywallet.transaction.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/transaction")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('USER')")
    fun deposit(@RequestBody request: DepositRequestDto): Mono<ResponseEntity<DepositResponseDto>> =
        transactionService.deposit(request)
            .map { ResponseEntity.ok(it) }
}
