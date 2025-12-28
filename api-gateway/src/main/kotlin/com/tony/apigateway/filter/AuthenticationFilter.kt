package com.tony.apigateway.filter

import model.constant.CustomHeaders
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private const val AUTHORIZATION = "Authorization"

@RefreshScope
@Component
class AuthenticationFilter(
    private val jwtUtil: JwtUtil
) : GatewayFilter {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        if (isAuthMissing(request)) return onError(exchange, HttpStatus.UNAUTHORIZED)

        val token = getAuthHeader(request).substring(7)
        if (jwtUtil.isInvalid(token)) return onError(exchange, HttpStatus.FORBIDDEN)

        val updatedExchange = updateRequest(exchange, token)
        return chain.filter(updatedExchange)
    }

    /**
     * Update the request with the user information. Add info to the request headers.
     */
    private fun updateRequest(exchange: ServerWebExchange, token: String?): ServerWebExchange =
        exchange.mutate().request(
            exchange.request.mutate()
                .header(CustomHeaders.X_USER_ID, jwtUtil.extractUserId(token))
                .header(CustomHeaders.X_ROLES, jwtUtil.extractRoles(token))
                .header(CustomHeaders.X_EMAIL, jwtUtil.extractEmail(token))
                .build()
        ).build()


    private fun onError(exchange: ServerWebExchange, httpStatus: HttpStatus?): Mono<Void> {
        exchange.response.statusCode = httpStatus
        return exchange.response.setComplete()
    }

    private fun getAuthHeader(request: ServerHttpRequest): String =
        request.headers.getOrEmpty(AUTHORIZATION).first()

    private fun isAuthMissing(request: ServerHttpRequest): Boolean =
        !request.headers.containsHeader(AUTHORIZATION)

}
