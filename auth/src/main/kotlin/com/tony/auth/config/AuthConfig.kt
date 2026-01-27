package com.tony.auth.config

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class KeycloakConfig {

    @Bean
    fun keycloak(): Keycloak = KeycloakBuilder.builder()
        .serverUrl("http://localhost:8081") //todo
        .realm("wallet-realm")
        .username("admin")
        .password("admin")
        .clientId("wallet-backend")
        .build()

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8081") ///todo
            .build()
    }
}