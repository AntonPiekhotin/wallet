package com.tony.common.model.event

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.common.model.constant.KafkaConstants.Topic.COMPENSATION
import com.tony.common.model.constant.SagaConstants.SagaType
import java.time.LocalDateTime

abstract class KafkaEvent {
    @get:JsonIgnore
    abstract val topic: String

    @get:JsonIgnore
    abstract val key: String
}

abstract class SagaEvent : KafkaEvent() {
    abstract val sagaId: String
    abstract val traceability: MutableMap<String, String>
    abstract val sagaType: SagaType
    val timestamp: LocalDateTime = LocalDateTime.now()
    override val key: String
        get() = sagaId
}

data class SagaCompensationEvent(
    override val sagaId: String,
    override val traceability: MutableMap<String, String>,
    override val sagaType: SagaType,
    val reason: String,
    val sourceService: String
) : SagaEvent() {
    override val topic: String = COMPENSATION
}