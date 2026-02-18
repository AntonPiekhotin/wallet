package com.tony.common.model.constant

class SagaConstants {

    enum class SagaOperation(name: String) {
        USER_CREATED("USER_CREATED"),
    }

    object Source {
        const val WALLET_SOURCE = "wallet-service"
        const val USER_SOURCE = "user-service"
    }
}
