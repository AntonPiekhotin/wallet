package com.tony.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.tony.auth.model.RegisterRequest
import com.tony.auth.util.UserCredentials
import com.tony.auth.util.KeycloakProperties
import model.exception.MyWalletException
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Component

@Component
class KeycloakService(
    private val keycloak: Keycloak,
    private val props: KeycloakProperties,
    private val mapper: ObjectMapper,
) {

    fun registerUser(request: RegisterRequest) = with(request) {
        val user = UserRepresentation().apply {
            username = email
            firstName = firstName
            lastName = lastName
            email = email
            credentials = mutableListOf(UserCredentials.createPasswordCredentials(password))
            isEnabled = true
        }
        val response = keycloak.realm(props.realm).users().create(user)
        if (response.status != 200) {
            val errorBody = response.readEntity(String::class.java)
            val errorMap = mapper.readValue(errorBody, Map::class.java) as Map<*, *>
            val errorMsg = errorMap["errorMessage"] ?: "Unknown error"
            throw MyWalletException(response.status, "Failed to create user: $errorMsg")
        }
    }
}
