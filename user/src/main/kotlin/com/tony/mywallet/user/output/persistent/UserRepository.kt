package com.tony.mywallet.user.output.persistent

import com.tony.mywallet.user.model.User
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, UUID> {

}
