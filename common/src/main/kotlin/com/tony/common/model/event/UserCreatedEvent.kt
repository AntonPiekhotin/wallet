package com.tony.common.model.event

import com.tony.common.model.constant.KafkaConstants
import java.time.LocalDateTime

data class UserCreatedEvent(
    val userId: String,
    val email: String,
    val role: String,
    val firstName: String,
    val lastName: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) : KafkaEvent {
    override val key = userId
    override val topic = KafkaConstants.Topic.USER_CREATED
}
