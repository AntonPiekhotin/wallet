package com.tony.common.model.event

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.common.model.constant.SagaOperation
import com.tony.common.model.constant.KafkaTopic.COMPENSATION
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
    abstract val sagaOperation: SagaOperation
    val timestamp: LocalDateTime = LocalDateTime.now()
    override val key: String
        get() = sagaId
}

data class SagaCompensationEvent(
    override val sagaId: String,
    override val traceability: MutableMap<String, String>,
    override val sagaOperation: SagaOperation,
    val reason: String,
    val sourceService: String
) : SagaEvent() {
    override val topic: String = COMPENSATION
}