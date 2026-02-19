package com.tony.mywallet.common.jpa.entity

import com.tony.common.model.constant.SagaOperation
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "saga_journal")
data class SagaJournalEntity(
    @Id
    @Column(length = 36)
    val sagaId: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val sagaType: SagaOperation,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    val payload: String,

    @CreationTimestamp
    val createdAt: Instant = Instant.now()
)
