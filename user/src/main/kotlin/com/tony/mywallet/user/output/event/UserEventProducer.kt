package com.tony.mywallet.user.output.event

import com.tony.common.model.event.KafkaEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class UserEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun sendEvent(event: KafkaEvent) {
        kafkaTemplate.send(
            event.topic,
            event.key,
            event
        ).also {
            logger.info("Sent event to topic ${event.topic} with key ${event.key}: $event")
        }
    }
}