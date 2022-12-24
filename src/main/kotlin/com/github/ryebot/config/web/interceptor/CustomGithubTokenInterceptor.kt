package com.github.ryebot.config.web.interceptor

import com.github.ryebot.config.web.interceptor.Interceptors.toRequestBody
import com.github.ryebot.domain.token.GithubTokenService
import kotlinx.coroutines.runBlocking
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 토큰 여부를 확인하기 위함
 */
class CustomGithubTokenInterceptor(
    private val githubTokenService: GithubTokenService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = runBlocking {

        val triggerRequest = request.toRequestBody()

        githubTokenService.saveTokenIfNull(triggerRequest.githubAppInstallationId!!)
        return@runBlocking true
    }
}
