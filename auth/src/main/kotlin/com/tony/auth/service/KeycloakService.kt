package com.tony.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.tony.auth.model.LoginRequest
import com.tony.auth.model.RegisterRequest
import com.tony.auth.model.TokenResponse
import com.tony.auth.output.event.AuthEventProducer
import com.tony.auth.util.KeycloakProperties
import com.tony.auth.util.UserCredentials
import com.tony.common.exception.MyWalletException
import com.tony.common.model.Role
import com.tony.common.model.constant.SagaContextKeys.USER_ID
import com.tony.common.model.event.UserCreatedEvent
import jakarta.ws.rs.core.Response
import java.util.*
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val GRANT_TYPE = "grant_type"
private const val PASSWORD = "password"
private const val CLIENT_ID = "client_id"
private const val CLIENT_SECRET = "client_secret"
private const val REFRESH_TOKEN = "refresh_token"
private const val USERNAME = "username"
private const val SCOPE = "scope"
private const val OPENID = "openid"

@Service
class KeycloakService(
    private val keycloak: Keycloak,
    private val props: KeycloakProperties,
    private val mapper: ObjectMapper,
    private val webClient: WebClient,
    private val eventProducer: AuthEventProducer
) {

    private val tokenUrl = "${props.url}/realms/${props.realm}/protocol/openid-connect/token"
    private val logoutUrl = "${props.url}/realms/${props.realm}/protocol/openid-connect/logout"

    fun registerUser(request: RegisterRequest) = with(request) {
        val user = UserRepresentation().apply {
            username = request.email
            firstName = request.firstName
            lastName = request.lastName
            email = request.email
            credentials = mutableListOf(UserCredentials.createPasswordCredentials(password))
            isEnabled = true
        }
        val response = keycloak.realm(props.realm).users().create(user)
        if (response.status != 200 && response.status != 201) {
            processError(response)
        }
        val userId = CreatedResponseUtil.getCreatedId(response)
        assignClientRole(userId)
        sendUserCreatedEvent(userId, user)
    }

    private fun sendUserCreatedEvent(userId: String, user: UserRepresentation) {
        eventProducer.sendEvent(
            UserCreatedEvent(
                sagaId = UUID.randomUUID().toString(),
                userId = userId,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                traceability = mutableMapOf(
                    USER_ID to userId
                )
            )
        )
    }

    private fun processError(response: Response): Nothing {
        val errorBody = response.readEntity(String::class.java)
        val errorMap = mapper.readValue(errorBody, Map::class.java) as Map<*, *>
        val errorMsg = errorMap["errorMessage"] ?: "Unknown error"
        throw MyWalletException(response.status, "Failed to create user: $errorMsg")
    }

    private fun assignClientRole(
        userId: String,
        role: Role = Role.USER
    ) {
        val realmResource = keycloak.realm(props.realm)
        val client = realmResource.clients()
            .findByClientId(props.clientId)
            .firstOrNull()
            ?: throw MyWalletException(HttpStatus.NOT_IMPLEMENTED.value(), "Client not found")
        val clientResource = realmResource.clients().get(client.id)
        val role = clientResource.roles().get(role.name).toRepresentation()
        realmResource.users()
            .get(userId)
            .roles()
            .clientLevel(client.id)
            .add(listOf(role))
    }

    fun login(request: LoginRequest): Mono<TokenResponse> {
        return webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData(GRANT_TYPE, PASSWORD)
                    .with(CLIENT_ID, props.clientId)
                    .with(CLIENT_SECRET, props.clientSecret)
                    .with(USERNAME, request.email)
                    .with(PASSWORD, request.password)
                    .with(SCOPE, OPENID)
            )
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono<String>().flatMap { errorBody ->
                    Mono.error(MyWalletException(response.statusCode().value(), "Login failed: $errorBody"))
                }
            }
            .bodyToMono<TokenResponse>()
    }

    fun refresh(refreshToken: String): Mono<TokenResponse> {
        return webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData(GRANT_TYPE, REFRESH_TOKEN)
                    .with(CLIENT_ID, props.clientId)
                    .with(CLIENT_SECRET, props.clientSecret)
                    .with(REFRESH_TOKEN, refreshToken)
            )
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono<String>().flatMap { errorBody ->
                    Mono.error(MyWalletException(response.statusCode().value(), "Refresh failed: $errorBody"))
                }
            }
            .bodyToMono<TokenResponse>()
    }

    fun logout(refreshToken: String): Mono<Void> {
        return webClient.post()
            .uri(logoutUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData(CLIENT_ID, props.clientId)
                    .with(CLIENT_SECRET, props.clientSecret)
                    .with(REFRESH_TOKEN, refreshToken)
            )
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono<String>().flatMap { errorBody ->
                    Mono.error(MyWalletException(response.statusCode().value(), "Logout failed: $errorBody"))
                }
            }
            .toBodilessEntity()
            .then()
    }

    fun deleteUser(userId: String) {
        val usersResource = keycloak.realm(props.realm).users()
        try {
            usersResource.get(userId).remove()
        } catch (ex: Exception) {
            throw MyWalletException(statusCode = 500, message = "Failed to delete user $userId", cause = ex)
        }
    }
}
