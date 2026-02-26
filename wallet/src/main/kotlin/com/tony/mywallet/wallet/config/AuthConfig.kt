package com.tony.mywallet.wallet.config

import com.tony.common.security.util.JwtAuthenticationConverter
import com.tony.common.security.util.JwtConverterProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
class AuthConfig(
    @Value($$"${keycloak.client-id}")
    private val clientId: String,
) {

    @Bean
    fun customAuthenticationConverter(): JwtAuthenticationConverter =
        JwtAuthenticationConverter(JwtConverterProperties(clientId))

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeExchange {
                it.pathMatchers("/admin/**").hasRole("ADMIN")
                it.anyExchange().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(customAuthenticationConverter())
                }
            }
            .build()
    }
}
