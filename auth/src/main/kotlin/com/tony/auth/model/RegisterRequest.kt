package com.tony.auth.model

//todo validation
data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)