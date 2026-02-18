package com.tony.mywallet.common.jpa.repo

import com.tony.mywallet.common.jpa.entity.SagaJournalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SagaJournalRepository : JpaRepository<SagaJournalEntity, String>
