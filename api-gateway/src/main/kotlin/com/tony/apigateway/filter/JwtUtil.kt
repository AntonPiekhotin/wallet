package com.tony.apigateway.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtUtil {
    @Value($$"${jwt.secret}")
    private val secret: String? = null

    private val signingKey: Key
        get() = Keys.hmacShaKeyFor(secret?.toByteArray(StandardCharsets.UTF_8))

    fun extractUserId(token: String?): String =
        extractAllClaimsFromToken(token).get("userId", String::class.java)

    fun extractRoles(token: String?): String =
        extractAllClaimsFromToken(token).get("roles", String::class.java)

    fun extractEmail(token: String?): String =
        extractAllClaimsFromToken(token).subject

    fun isInvalid(token: String?): Boolean =
        isTokenExpired(token)

    private fun isTokenExpired(token: String?): Boolean =
        extractAllClaimsFromToken(token).expiration.before(Date())

    fun extractAllClaimsFromToken(token: String?): Claims =
        Jwts.parser()
            .setSigningKey(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
}
