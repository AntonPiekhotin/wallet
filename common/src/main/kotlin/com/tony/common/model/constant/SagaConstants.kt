package com.tony.common.model.constant

enum class SagaOperation(name: String) {
    USER_CREATED("USER_CREATED"),
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    P2P_TRANSFER("P2P_TRANSFER"),
}

object SagaSource {
    const val WALLET_SOURCE = "wallet-service"
    const val USER_SOURCE = "user-service"
    const val TRANSACTION_SOURCE = "transaction-service"
}

object SagaContextKeys {
    const val USER_ID = "user.id"
    const val WALLET_ID = "wallet.id"
}