package com.tony.mywallet.user.config

import com.tony.mywallet.user.util.JwtAuthenticationConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
class AuthConfig(
    private val customAuthenticationConverter: JwtAuthenticationConverter
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeExchange {
                it.pathMatchers("/admin/**").hasRole("ADMIN")
                it.anyExchange().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(customAuthenticationConverter)
                }
            }
            .build()
    }

//    @Bean
//    fun jwtAuthenticationConverter(): Converter<Jwt, Mono<AbstractAuthenticationToken>> {
//        return Converter<Jwt, Mono<AbstractAuthenticationToken>> { jwt ->
//            val roles = extractRoles(jwt)
//            val principal = UserPrincipal(
//                userId = jwt.subject,
//                email = jwt.getClaim("email"),
//                roles = roles
//            )
//            val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
//            Mono.just(
//                UsernamePasswordAuthenticationToken(
//                    principal,
//                    null,
//                    authorities
//                )
//            )
//        }
//    }

}
