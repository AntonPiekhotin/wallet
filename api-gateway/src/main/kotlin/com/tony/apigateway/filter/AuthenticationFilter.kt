package com.tony.apigateway.filter

import io.jsonwebtoken.Claims
import java.lang.String
import kotlin.Boolean
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private const val AUTHORIZATION = "Authorization"
private const val EMAIL = "email"

@RefreshScope
@Component
class AuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val routerValidator: RouterValidator
) : GatewayFilter {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request: ServerHttpRequest = exchange.request

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) return onError(exchange, HttpStatus.UNAUTHORIZED)
            val token = this.getAuthHeader(request).substring(7)

            if (jwtUtil.isInvalid(token)) return onError(exchange, HttpStatus.FORBIDDEN)
            updateRequest(exchange, token)
        }
        return chain.filter(exchange)
    }

    private fun onError(exchange: ServerWebExchange, httpStatus: HttpStatus?): Mono<Void> {
        exchange.response.statusCode = httpStatus
        return exchange.response.setComplete()
    }

    private fun getAuthHeader(request: ServerHttpRequest): kotlin.String =
        request.headers.getOrEmpty(AUTHORIZATION).first()

    private fun isAuthMissing(request: ServerHttpRequest): Boolean =
        !request.headers.containsHeader(AUTHORIZATION)

    private fun updateRequest(exchange: ServerWebExchange, token: kotlin.String) {
        val claims: Claims = jwtUtil.extractAllClaimsFromToken(token)
        exchange.request.mutate()
            .header(EMAIL, String.valueOf(claims[EMAIL]))
            .build()
    }
}
