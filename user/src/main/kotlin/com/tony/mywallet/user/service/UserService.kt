package com.tony.mywallet.user.service

import com.tony.mywallet.user.model.User
import com.tony.mywallet.user.output.persistent.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createUser(user: User) {
        logger.info("Creating user ${user.id}")
        userRepository.save(user)
    }
}
