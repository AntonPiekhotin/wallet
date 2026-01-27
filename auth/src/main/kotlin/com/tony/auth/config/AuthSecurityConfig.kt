package com.tony.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class AuthSecurityConfig {

    @Bean
    fun chain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http.csrf { it.disable() }
            .authorizeExchange { it.anyExchange().permitAll() }
            .build()
}
