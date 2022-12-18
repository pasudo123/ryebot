package com.github.ryebot.config.web.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.config.mapper.toObject
import com.github.ryebot.config.web.CustomRequestServletWrapper
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 로깅을 남기기 위함
 */
class CustomLoggingInterceptor(
    private val mapper: ObjectMapper
) : HandlerInterceptor {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val builder = StringBuilder()

        if (request !is CustomRequestServletWrapper) {
            log.error("request.check : $request")
            return false
        }

        val requestBody = request.toRequestBody()

        builder.apply {
            this.appendLine()
            this.appendLine("=================================>")
            this.appendLine("request.uri : ${request.requestURI}")
            this.appendLine("request.method : ${request.method}")

            try {
                val triggerRequest = requestBody.toObject<TriggerRequest>()
                this.appendLine("request.trigger.action : ${triggerRequest.action}")
            } catch (_: Exception) {}

            this.appendLine("=================================>")
            this.appendLine()
        }
        log.info(builder.toString())

        return true
    }
}
