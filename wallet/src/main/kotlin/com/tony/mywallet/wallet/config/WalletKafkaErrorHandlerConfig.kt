package com.tony.mywallet.wallet.config

import com.tony.common.model.constant.SagaSource.WALLET_SOURCE
import com.tony.common.model.event.SagaCompensationEvent
import com.tony.common.model.event.UserCreatedEvent
import com.tony.mywallet.wallet.output.event.WalletEventProducer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.listener.ConsumerRecordRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class WalletKafkaErrorHandlerConfig(
    private val walletEventProducer: WalletEventProducer
) {

    @Bean
    fun errorHandler(): DefaultErrorHandler {
        val backOff = FixedBackOff(3000L, 3)
        val recoverer = ConsumerRecordRecoverer { consumerRecord, exception ->
            val event = consumerRecord.value() as? UserCreatedEvent
            if (event != null) {
                walletEventProducer.sendEvent(
                    SagaCompensationEvent(
                        sagaId = event.sagaId,
                        traceability = event.traceability,
                        reason = "Exhausted retries: " + exception.cause?.message,
                        sourceService = WALLET_SOURCE,
                        sagaOperation = event.sagaOperation,
                    )
                )
            }
        }
        return DefaultErrorHandler(recoverer, backOff)
    }
}
