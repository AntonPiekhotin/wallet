package com.tony.mywallet.common.jpa.store

import com.fasterxml.jackson.databind.ObjectMapper
import com.tony.common.exception.MyWalletException
import com.tony.common.model.constant.SagaOperation
import com.tony.mywallet.common.jpa.entity.SagaJournalEntity
import com.tony.mywallet.common.jpa.repo.SagaJournalRepository
import com.tony.mywallet.common.jpa.repo.SagaPayload
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class SagaStore(
    private val repository: SagaJournalRepository,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(SagaStore::class.java)

    fun <T : SagaPayload> saveContext(sagaId: String, sagaOperation: SagaOperation, context: T) {
        try {
            val jsonPayload = objectMapper.writeValueAsString(context)
            val entity = SagaJournalEntity(sagaId, sagaOperation, jsonPayload)
            repository.save(entity)
        } catch (e: Exception) {
            logger.error("Saga [$sagaId]: Failed to serialize context of type ${context::class.simpleName}", e)
            throw MyWalletException(500, "Failed to serialize Saga context", e)
        }
    }

    fun <T : SagaPayload> getContext(sagaId: String, targetType: Class<T>): T? {
        val entity = repository.findByIdOrNull(sagaId) ?: return null
        return try {
            objectMapper.readValue(entity.payload, targetType)
        } catch (e: Exception) {
            logger.error(
                "Saga [$sagaId]: CRITICAL! Failed to parse JSON into ${targetType.simpleName}. " +
                        "Payload from DB: '${entity.payload}'", e
            )
            throw MyWalletException(
                500, "Cannot deserialize saga context for $sagaId to ${targetType.simpleName}", e
            )
        }
    }

    fun deleteContext(sagaId: String) {
        repository.deleteById(sagaId)
    }
}
