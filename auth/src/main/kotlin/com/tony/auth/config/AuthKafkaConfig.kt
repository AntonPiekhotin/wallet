package com.tony.auth.config

import com.tony.common.model.constant.KafkaConstants
import kotlin.Any
import kotlin.String
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer
import org.springframework.kafka.support.serializer.JacksonJsonSerializer
import org.springframework.util.backoff.FixedBackOff

@Configuration
@EnableKafka
class AuthKafkaConfig {

    @Value("\${kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val props: MutableMap<String, Any> = hashMapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JacksonJsonSerializer::class.java,

            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.RETRIES_CONFIG to 3,
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true,
            ProducerConfig.LINGER_MS_CONFIG to 5,
            ProducerConfig.BATCH_SIZE_CONFIG to 32_768
        )
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> =
        KafkaTemplate(producerFactory())

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> =
        DefaultKafkaConsumerFactory(
            mutableMapOf<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG to KafkaConstants.Group.AUTH_SERVICE,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JacksonJsonDeserializer::class.java,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 500,
                ConsumerConfig.FETCH_MIN_BYTES_CONFIG to 1,
                ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG to 500,

                "spring.json.trusted.packages" to "*",
                "spring.json.value.default.type" to "com.tony.common.model.event.KafkaEvent"
            )
        )

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> =
        ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            setConsumerFactory(consumerFactory())
            setConcurrency(1)
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
            setCommonErrorHandler(
                DefaultErrorHandler(
                    FixedBackOff(10_000L, 3)
                )
            )
        }
}
