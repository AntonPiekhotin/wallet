package com.tony.auth.service

import com.tony.auth.model.LoginRequest
import com.tony.auth.model.RefreshRequest
import com.tony.auth.model.RegisterRequest
import model.exception.MyWalletException
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val oidc: KeycloakOidcClient,
    private val admin: KeycloakAdminClient
) {

    fun login(req: LoginRequest) =
        oidc.login(req.email, req.password)

    fun refresh(req: RefreshRequest) =
        oidc.refresh(req.refreshToken)

    fun logout(req: RefreshRequest) =
        oidc.logout(req.refreshToken)

    fun register(req: RegisterRequest): Mono<Void> =
        Mono.fromRunnable { admin.registerUser(req) }
}
