package com.tony.mywallet.common.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "saga_journal")
data class SagaJournalEntity(
    @Id
    @Column(length = 36)
    val sagaId: String,

    @Column(nullable = false)
    val sagaType: String,

    @Column(columnDefinition = "jsonb", nullable = false)
    val payload: String,

    @CreationTimestamp
    val createdAt: Instant = Instant.now()
)
