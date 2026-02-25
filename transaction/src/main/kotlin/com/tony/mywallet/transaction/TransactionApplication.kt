package com.tony.mywallet.transaction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(
    scanBasePackages = ["com.tony.mywallet.transaction", "com.tony.mywallet.common.jpa"]
)
@EntityScan(
    basePackages = ["com.tony.mywallet.transaction", "com.tony.mywallet.common.jpa"]
)
@EnableJpaRepositories(
    basePackages = ["com.tony.mywallet.transaction", "com.tony.mywallet.common.jpa"]

)class TransactionApplication

fun main(args: Array<String>) {
    runApplication<TransactionApplication>(*args)
}
