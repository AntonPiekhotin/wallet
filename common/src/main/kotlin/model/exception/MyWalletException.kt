package model.exception

class MyWalletException(
    val statusCode: Int,
) : RuntimeException()
