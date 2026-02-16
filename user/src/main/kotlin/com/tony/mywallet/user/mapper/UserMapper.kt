package com.tony.mywallet.user.mapper

import com.tony.common.model.dto.UserDto
import com.tony.common.model.event.UserCreatedEvent
import com.tony.mywallet.user.model.User
import java.util.UUID

object UserMapper {

    fun User.toUserDto() = UserDto(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        createdAt = createdAt,
    )

    fun UserDto.toUserEntity() = User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        createdAt = createdAt,
    )

    fun UserCreatedEvent.toUserDto() = UserDto(
        id = UUID.fromString(userId),
        firstName = firstName,
        lastName = lastName,
        email = email,
        createdAt = createdAt,
    )

    fun UserCreatedEvent.toUserEntity() = User(
        id = UUID.fromString(userId),
        firstName = firstName,
        lastName = lastName,
        email = email,
        createdAt = createdAt,
    )
}