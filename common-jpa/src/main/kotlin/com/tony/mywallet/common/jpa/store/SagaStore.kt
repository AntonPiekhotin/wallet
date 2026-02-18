package com.tony.mywallet.common.jpa.store

import com.fasterxml.jackson.databind.ObjectMapper
import com.tony.mywallet.common.jpa.entity.SagaJournalEntity
import com.tony.mywallet.common.jpa.repo.SagaJournalRepository
import com.tony.mywallet.common.jpa.repo.SagaPayload
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class SagaStore(
    private val repository: SagaJournalRepository,
    private val objectMapper: ObjectMapper
) {

    fun <T : SagaPayload> saveContext(sagaId: String, sagaType: String, context: T) {
        val jsonPayload = objectMapper.writeValueAsString(context)
        val entity = SagaJournalEntity(sagaId, sagaType, jsonPayload)
        repository.save(entity)
    }

    fun <T : SagaPayload> getContext(sagaId: String, targetType: Class<T>): T? {
        val entity = repository.findByIdOrNull(sagaId) ?: return null
        return objectMapper.readValue(entity.payload, targetType)
    }

    fun deleteContext(sagaId: String) {
        repository.deleteById(sagaId)
    }
}
