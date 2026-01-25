package com.tony.auth.service

import com.tony.auth.model.KeycloakTokenResponse
import com.tony.auth.model.TokenResponse
import com.tony.auth.util.KeycloakProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val CLIENT_ID = "client_id"
private const val CLIENT_SECRET = "client_secret"
private const val REFRESH_TOKEN = "refresh_token"
private const val GRANT_TYPE = "grant_type"
private const val USERNAME = "username"
private const val PASSWORD = "password"

@Component
class KeycloakOidcClient(
    private val webClient: WebClient,
    private val props: KeycloakProperties
) {

    private fun tokenUri() =
        "${props.url}/realms/${props.realm}/protocol/openid-connect/token"

    fun login(username: String, password: String): Mono<TokenResponse> =
        webClient.post()
            .uri(tokenUri())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData(GRANT_TYPE, PASSWORD)
                    .with(CLIENT_ID, props.clientId)
                    .with(CLIENT_SECRET, props.clientSecret)
                    .with(USERNAME, username)
                    .with(PASSWORD, password)
            )
            .retrieve()
            .bodyToMono<KeycloakTokenResponse>()
            .map {
                TokenResponse(it.access_token, it.refresh_token, it.expires_in)
            }

    fun refresh(refreshToken: String): Mono<TokenResponse> =
        webClient.post()
            .uri(tokenUri())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData(GRANT_TYPE, REFRESH_TOKEN)
                    .with(CLIENT_ID, props.clientId)
                    .with(CLIENT_SECRET, props.clientSecret)
                    .with(REFRESH_TOKEN, refreshToken)
            )
            .retrieve()
            .bodyToMono<KeycloakTokenResponse>()
            .map {
                TokenResponse(it.access_token, it.refresh_token, it.expires_in)
            }

    fun logout(refreshToken: String): Mono<Void> =
        webClient.post()
            .uri("${props.url}/realms/${props.realm}/protocol/openid-connect/logout")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData(CLIENT_ID, props.clientId)
                    .with(CLIENT_SECRET, props.clientSecret)
                    .with(REFRESH_TOKEN, refreshToken)
            )
            .retrieve()
            .bodyToMono<Void>()
}
