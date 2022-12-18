package com.github.ryebot.config.web.interceptor

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.config.mapper.toObject
import com.github.ryebot.config.web.CustomRequestServletWrapper
import javax.servlet.http.HttpServletRequest

object Interceptors {

    fun HttpServletRequest.toRequestBody(): TriggerRequest {
        return (this as CustomRequestServletWrapper)
            .toRequestBody()
            .toObject()
    }
}