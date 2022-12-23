package com.github.ryebot.error

data class ErrorWrapper(
    val message: String,
    val errorCode: ErrorCode,
    val appendProperties: Map<String, String> = emptyMap()
)
