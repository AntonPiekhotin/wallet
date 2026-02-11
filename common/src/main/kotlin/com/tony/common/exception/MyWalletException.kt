package com.tony.common.exception

class MyWalletException(
    val statusCode: Int,
    override val message: String? = null
) : RuntimeException()
