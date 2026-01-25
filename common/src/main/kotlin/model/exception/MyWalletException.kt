package model.exception

class MyWalletException(
    val statusCode: Int,
    override val message: String? = null
) : RuntimeException()
