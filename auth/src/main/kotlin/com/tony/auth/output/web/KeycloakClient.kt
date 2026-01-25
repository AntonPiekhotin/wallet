package com.tony.auth.output.web

import com.tony.auth.model.TokenResponse
import com.tony.auth.util.KeycloakProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class KeycloakClient(
    private val webClient: WebClient,
    private val keycloakProperties: KeycloakProperties
) {

    fun login(username: String, password: String): Mono<TokenResponse> {
        return webClient.post()
            .uri("${keycloakProperties.url}/realms/${keycloakProperties.realm}/protocol/openid-connect/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("grant_type", "password")
                .with("client_id", keycloakProperties.clientId)
                .with("client_secret", keycloakProperties.clientSecret)
                .with("username", username)
                .with("password", password))
            .retrieve()
            .bodyToMono(::class.java)
            .map {
                TokenResponse(
                    it.access_token,
                    it.refresh_token,
                    it.expires_in
                )
            }
    }
}