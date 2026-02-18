package com.tony.common.model.event

import com.tony.common.model.Role
import com.tony.common.model.constant.KafkaConstants.SagaContextKeys.USER_ID
import com.tony.common.model.constant.KafkaConstants.Topic.USER_CREATED

data class UserCreatedEvent(
    override val sagaId: String,
    override val traceability: MutableMap<String, String> = mutableMapOf(),
    val userId: String,
    val email: String,
    val role: String = Role.USER.name,
    val firstName: String,
    val lastName: String,
) : SagaEvent() {
    override val topic = USER_CREATED
    init {
        traceability[USER_ID] = userId
    }
}
