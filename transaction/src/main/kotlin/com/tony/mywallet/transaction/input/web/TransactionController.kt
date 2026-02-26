package com.tony.mywallet.transaction.input.web

import com.tony.mywallet.transaction.model.dto.DepositRequestDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transaction")
class TransactionController {

    // remember auth
    
    @PostMapping("/deposit")
    fun deposit(@RequestBody request: DepositRequestDto) {
        // Implementation will follow
    }

}
