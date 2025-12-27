package com.tony.apigateway.filter

import java.util.function.Predicate
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class RouterValidator {

    private val allowedRoutes = listOf(
        "/auth/sign-in",
        "/auth/sign-up"
    )

    val isSecured: Predicate<ServerHttpRequest> = Predicate { request ->
        allowedRoutes.none { route -> request.uri.path.contains(route) }
    }
}