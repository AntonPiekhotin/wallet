package com.tony.common.model.constant

class SagaConstants {

    enum class SagaType(name: String) {
        USER_CREATED ("USER_CREATED"),
    }

    object Source {
        const val WALLET_SERVICE = "wallet-service"
        const val USER_SERVICE = "user-service"
    }
}
