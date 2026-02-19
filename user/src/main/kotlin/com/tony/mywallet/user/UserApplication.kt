package com.tony.mywallet.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(
    scanBasePackages = ["com.tony.mywallet.user", "com.tony.mywallet.common.jpa"]
)
@EntityScan(
    basePackages = ["com.tony.mywallet.user", "com.tony.mywallet.common.jpa"]
)
@EnableJpaRepositories(
    basePackages = ["com.tony.mywallet.user", "com.tony.mywallet.common.jpa"]

)
class UserApplication

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args)
}
