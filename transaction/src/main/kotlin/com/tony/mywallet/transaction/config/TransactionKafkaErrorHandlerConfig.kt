package com.tony.mywallet.transaction.config

import com.tony.common.model.event.UserCreatedEvent
import com.tony.mywallet.transaction.output.event.TransactionEventProducer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.listener.ConsumerRecordRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class TransactionKafkaErrorHandlerConfig(
    private val transactionEventProducer: TransactionEventProducer
) {

    @Bean
    fun errorHandler(): DefaultErrorHandler {
        val backOff = FixedBackOff(3000L, 3)
        val recoverer = ConsumerRecordRecoverer { consumerRecord, exception ->
            val event = consumerRecord.value() as? UserCreatedEvent //todo: what is happening here???
            if (event != null) {
//                transactionEventProducer.sendEvent(
//                    SagaCompensationEvent(
//                        sagaId = event.sagaId,
//                        traceability = event.traceability,
//                        reason = "Exhausted retries: " + exception.cause?.message,
//                        sourceService = WALLET_SOURCE,
//                        sagaOperation = event.sagaOperation,
//                    )
//                )
//                transactionEventProducer.sendCompensationEvent(event, exception)
            }
        }
        return DefaultErrorHandler(recoverer, backOff)
    }
}
