package com.tony.auth.exception

import java.util.Arrays
import com.tony.common.model.dto.ResponseErrorDto
import com.tony.common.exception.MyWalletException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(MyWalletException::class)
    fun handleMyWalletFlowException(e: MyWalletException): ResponseEntity<ResponseErrorDto> =
        ResponseEntity.status(e.statusCode).body(
            ResponseErrorDto(
                statusCode = e.statusCode,
                errorMessage = listOf(e.message ?: "No message available"),
                stackTrace = Arrays.stream(e.stackTrace)
                    .map(StackTraceElement::toString)
                    .toList()
            )
        )

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<ResponseErrorDto> =
        ResponseEntity.status(500).body(
            ResponseErrorDto(
                statusCode = 500,
                errorMessage = listOf(e.message ?: "No message available"),
                stackTrace = Arrays.stream(e.stackTrace)
                    .map(StackTraceElement::toString)
                    .toList()
            )
        )
}
