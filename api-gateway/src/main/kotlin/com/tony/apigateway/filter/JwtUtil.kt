package com.tony.apigateway.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.Date
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    secret: String
) {

    private val key: Key =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun extractAllClaimsFromToken(token: String): Claims =
        Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .body

    fun isExpired(token: String): Boolean = extractAllClaimsFromToken(token).expiration.before(Date())

    fun isInvalid(token: String): Boolean = isExpired(token)
}
