package com.tony.mywallet.user.input.web

import com.tony.common.model.UserPrincipal
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController {

    @GetMapping("/me")
    fun me(authentication: Authentication): UserPrincipal {
        val user = authentication.principal as UserPrincipal
        return user
    }
}