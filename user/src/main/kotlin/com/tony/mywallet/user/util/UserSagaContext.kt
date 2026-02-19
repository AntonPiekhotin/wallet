package com.tony.mywallet.user.util

import com.tony.mywallet.common.jpa.repo.SagaPayload
import java.util.UUID

sealed class UserSagaContext : SagaPayload {

    data class Creation(
        val userId: UUID
    ) : UserSagaContext()

}
