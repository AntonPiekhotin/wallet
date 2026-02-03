package com.tony.auth.service

import com.tony.auth.model.LoginRequest
import com.tony.auth.model.RefreshRequest
import com.tony.auth.model.RegisterRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val keycloakService: KeycloakService
) {

    fun login(request: LoginRequest) =
        keycloakService.login(request)

    fun refresh(request: RefreshRequest) =
        keycloakService.refresh(request.refreshToken)

    fun logout(request: RefreshRequest) =
        keycloakService.logout(request.refreshToken)

    fun register(request: RegisterRequest): Mono<Void> =
        Mono.fromRunnable { keycloakService.registerUser(request) }
}
