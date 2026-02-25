package com.tony.common.model.constant

enum class SagaOperation(name: String) {
    USER_CREATED("USER_CREATED"),
}

object Source {
    const val WALLET_SOURCE = "wallet-service"
    const val USER_SOURCE = "user-service"
    const val TRANSACTION_SOURCE = "transaction-service"
}
