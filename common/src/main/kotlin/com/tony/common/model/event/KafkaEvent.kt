package com.tony.common.model.event

interface KafkaEvent {
    val key: String
    val topic: String
}