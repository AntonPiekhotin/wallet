package com.tony.apigateway.config

import com.tony.apigateway.filter.AuthenticationFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ApiGatewayConfig(
    private val filter: AuthenticationFilter
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
                    .filters { f -> f.filter(filter) }
                    .uri("lb://auth")
            }
            .build()
}