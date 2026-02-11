package com.tony.common.model.constant

class KafkaConstants {

    data class Group(val name: String) {
        companion object {
            const val AUTH_SERVICE = "auth-service"
            const val NOTIFICATION_SERVICE = "notification-service"
            const val USER_SERVICE = "user-service"
            const val WALLET_SERVICE = "wallet-service"
        }
    }
    data class Topic(val name: String) {
        companion object {
            const val USER_CREATED = "auth.user.created.v1"
            const val USER_UPDATED = "auth.user.updated.v1"
            const val USER_DELETED = "auth.user.deleted.v1"
        }
    }
}