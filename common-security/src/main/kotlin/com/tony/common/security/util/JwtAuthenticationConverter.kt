package com.tony.common.security.util

import com.tony.common.model.UserPrincipal
import kotlin.collections.get
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import reactor.core.publisher.Mono

class JwtAuthenticationConverter(
    private val properties: JwtConverterProperties
) : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken> {
        return Mono.just(
            UsernamePasswordAuthenticationToken(
                UserPrincipal(
                    userId = source.subject.orEmpty(),
                    email = source.getClaim("email"),
                    roles = extractRoles(source)
                ),
                null,
                extractRoles(source).map { SimpleGrantedAuthority("ROLE_$it") }
            )
        )
    }

    private fun extractRoles(jwt: Jwt): Set<String> {
        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access") ?: return emptySet()
        val client = resourceAccess[properties.clientId] as? Map<*, *> ?: return emptySet()
        val roles = client["roles"] as? Collection<*> ?: return emptySet()
        return roles.filterIsInstance<String>().toSet()
    }
}

data class JwtConverterProperties(
    val clientId: String
)
