package com.tony.mywallet.user.util

import com.tony.common.model.UserPrincipal
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationConverter(
    @Value($$"${keycloak.client-id}")
    private val clientId: String
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
        val client = resourceAccess[clientId] as? Map<*, *> ?: return emptySet()
        val roles = client["roles"] as? Collection<*> ?: return emptySet()
        return roles.filterIsInstance<String>().toSet()
    }
}