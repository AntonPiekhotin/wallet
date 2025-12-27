package com.tony.auth.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/v1/auth"])
class AuthController {

    @GetMapping("/sign-in")
    fun login(): String {
        return "Login"
    }
}