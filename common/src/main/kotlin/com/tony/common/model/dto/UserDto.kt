package com.tony.common.model.dto

import java.time.LocalDateTime
import java.util.UUID

data class UserDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: LocalDateTime,
)