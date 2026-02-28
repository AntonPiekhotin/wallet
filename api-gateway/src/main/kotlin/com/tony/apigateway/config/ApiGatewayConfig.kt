package com.tony.apigateway.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ApiGatewayConfig(
) {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes()
            .route("auth") {
                it.path("/api/v1/auth/**")
                    .uri("lb://auth")
            }
            .route("wallet") {
                it.path("/api/v1/wallet/**")
                    .uri("lb://wallet")
            }
            .route("user") {
                it.path("/api/v1/user/**")
                    .uri("lb://user")
            }
            .route("transaction") {
                it.path("/api/v1/transaction/**")
                    .uri("lb://transaction")
            }
            .build()
}
