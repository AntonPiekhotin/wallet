package com.tony.auth.service

import com.tony.auth.model.LoginRequest
import com.tony.auth.model.RefreshRequest
import com.tony.auth.model.RegisterRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val oidc: KeycloakOidcClient,
    private val keycloakService: KeycloakService
) {

    fun login(req: LoginRequest) =
        oidc.login(req.email, req.password)

    fun refresh(req: RefreshRequest) =
        oidc.refresh(req.refreshToken)

    fun logout(req: RefreshRequest) =
        oidc.logout(req.refreshToken)

    fun register(req: RegisterRequest): Mono<Void> =
        Mono.fromRunnable { keycloakService.registerUser(req) }
}
