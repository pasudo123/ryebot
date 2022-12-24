package com.github.ryebot.config.async

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class CustomAsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleUncaughtException(
        ex: Throwable,
        method: Method,
        vararg params: Any?
    ) {
        log.error("exception :: ${ex.message}")
    }
}
