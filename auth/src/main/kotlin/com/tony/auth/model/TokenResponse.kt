package com.tony.auth.model

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)