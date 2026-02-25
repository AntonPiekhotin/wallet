package com.tony.common.model.constant

class KafkaConstants { //todo: remove class

    object Group {
        const val AUTH_SERVICE = "auth-service"
        const val NOTIFICATION_SERVICE = "notification-service"
        const val USER_SERVICE = "user-service"
        const val WALLET_SERVICE = "wallet-service"
        const val TRANSACTION_SERVICE = "transaction-service"
    }

    object Topic {
        const val COMPENSATION = "saga.compensation"
        const val USER_CREATED = "auth.user.created.v1"
        const val USER_UPDATED = "auth.user.updated.v1"
        const val USER_DELETED = "auth.user.deleted.v1"
        const val DEPOSIT = "tx.deposit.initiated.v1"
        const val WITHDRAWAL = "tx.withdrawal.initiated.v1"
        const val P2P_TRANSFER = "tx.p2p.transfer.initiated.v1"
    }

    object SagaContextKeys {
        const val USER_ID = "user.id"
        const val WALLET_ID = "wallet.id"
    }
}
