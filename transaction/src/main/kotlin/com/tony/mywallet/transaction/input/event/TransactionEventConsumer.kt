package com.tony.mywallet.transaction.input.event

import com.tony.common.model.constant.KafkaGroup.TRANSACTION_SERVICE
import com.tony.common.model.constant.KafkaTopic.COMPENSATION
import com.tony.common.model.event.SagaCompensationEvent
import com.tony.common.saga.handler.CompensationHandler
import com.tony.common.saga.handler.SagaDispatcher
import com.tony.mywallet.common.jpa.store.SagaStore
import com.tony.mywallet.transaction.service.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class UserEventConsumer(
    private val transactionService: TransactionService,
    handlers: List<CompensationHandler>,
    private val sagaStore: SagaStore
) {
    private val dispatcher = SagaDispatcher(handlers)
    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = [COMPENSATION],
        groupId = TRANSACTION_SERVICE,
        containerFactory = "kafkaListenerContainerFactory",
    )
    fun onCompensation(event: SagaCompensationEvent, acknowledgment: Acknowledgment) {
        logger.debug("Received compensation event: {}", event)
        try {
            dispatcher.dispatch(event)
        } finally {
            acknowledgment.acknowledge()
        }
    }
}
