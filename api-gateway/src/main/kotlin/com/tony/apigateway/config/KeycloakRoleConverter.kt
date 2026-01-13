package com.tony.apigateway.config

import java.util.stream.Collectors
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class KeycloakRoleConverter : Converter<Jwt, MutableCollection<GrantedAuthority?>?> {
    override fun convert(jwt: Jwt): MutableCollection<GrantedAuthority?> {

        val realmAccess: MutableMap<String?, Any?>? = jwt.getClaim("realm_access")
        if (realmAccess.isNullOrEmpty()) return mutableListOf()

        return (realmAccess["roles"] as MutableList<String?>).stream()
            .map { roleName: String? -> "ROLE_$roleName" }
            .map { authority: String? -> SimpleGrantedAuthority(authority!!) }
            .collect(Collectors.toList())
    }
}
