package com.github.ryebot.infra.client

import retrofit2.Response

object ClientHelper

fun <T> Response<T>.getErrorBody(): String {
    return this.errorBody()?.string() ?: ""
}
