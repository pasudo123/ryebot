package com.github.ryebot.infra.client

import com.github.ryebot.error.ApiException
import com.github.ryebot.error.ErrorCode
import com.github.ryebot.error.ErrorWrapper
import retrofit2.Response

/**
 * error 패키지는 여기에 응집시킨다.
 */
object ClientHelper

fun <T> Response<T>.throwApiException(message: String) {
    throw ApiException(
        ErrorWrapper(
            message = "$message : ${this.getErrorBody()}",
            ErrorCode.API001
        )
    )
}

fun <T> Response<T>.getErrorBody(): String {
    return this.errorBody()?.string() ?: ""
}
