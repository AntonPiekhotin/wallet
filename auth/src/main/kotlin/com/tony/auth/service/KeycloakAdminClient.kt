package com.tony.auth.service

import com.tony.auth.model.RegisterRequest
import com.tony.auth.util.KeycloakProperties
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Component

@Component
class KeycloakAdminClient(props: KeycloakProperties) {

    private val keycloak: Keycloak = KeycloakBuilder.builder()
        .serverUrl(props.url)
        .realm("master")
        .username(props.adminUsername)
        .password(props.adminPassword)
        .clientId("admin-cli")
        .build()

    private val realm = props.realm

    fun registerUser(req: RegisterRequest) {
        val user = UserRepresentation().apply {
            username = req.email
            email = req.email
            isEnabled = true
        }

        val response = keycloak.realm(realm).users().create(user)
        if (response.status != 201) {
            throw RuntimeException("Failed to create user: ${response.status}")
        }

        val userId = response.location.path.split("/").last()

        val credential = CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = req.password
            isTemporary = false
        }

        keycloak.realm(realm)
            .users()[userId]
            .resetPassword(credential)
    }
}
