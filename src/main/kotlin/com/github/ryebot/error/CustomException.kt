package com.github.ryebot.error

open class CustomException(
    message: String
) : RuntimeException(message)

class ApiException(
    private val errorWrapper: ErrorWrapper
) : CustomException(errorWrapper.message)
