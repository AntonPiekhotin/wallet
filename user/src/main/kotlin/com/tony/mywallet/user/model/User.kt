package com.tony.mywallet.user.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(

    @Id
    val id: UUID,

    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: LocalDateTime
)
