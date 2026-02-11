package com.tony.mywallet.wallet.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/wallet")
class WalletController {

    @GetMapping("/test")
    fun test(): String {
        return "Wallet Service is up and running!"
    }
}
