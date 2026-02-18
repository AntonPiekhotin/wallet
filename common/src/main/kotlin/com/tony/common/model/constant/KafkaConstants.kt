package com.tony.common.model.constant

class KafkaConstants {

    object Group {
        const val AUTH_SERVICE = "auth-service"
        const val NOTIFICATION_SERVICE = "notification-service"
        const val USER_SERVICE = "user-service"
        const val WALLET_SERVICE = "wallet-service"
    }

    object Topic {
        const val COMPENSATION = "saga.compensation"
        const val USER_CREATED = "auth.user.created.v1"
        const val USER_UPDATED = "auth.user.updated.v1"
        const val USER_DELETED = "auth.user.deleted.v1"
    }

    object SagaContextKeys {
        const val USER_ID = "user.id"
        const val WALLET_ID = "wallet.id"
    }
}