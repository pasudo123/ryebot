package com.github.ryebot.config.web.interceptor

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.config.mapper.toObject
import com.github.ryebot.config.web.CustomRequestServletWrapper
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.repository.ActionRepository
import kotlinx.coroutines.runBlocking
import org.springframework.web.servlet.HandlerInterceptor
import retrofit2.awaitResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 토큰 여부를 확인하기 위함
 */
class CustomGithubTokenInterceptor(
    private val githubApiClient: GithubApiClient,
    private val actionRepository: ActionRepository
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = runBlocking {

        actionRepository.getTokenOrNull() ?: return@runBlocking true

        val triggerRequset = (request as CustomRequestServletWrapper)
            .toRequestBody()
            .toObject<TriggerRequest>()

        val installationId = triggerRequset.installation.id
        val githubApiResponse = githubApiClient
            .createTokenByInstallId(installationId)
            .awaitResponse()

        with(githubApiResponse) {
            if (this.isSuccessful.not()) {
                throw RuntimeException("token 조회 시 에러 발생 : ${this.errorBody()?.string()}")
            }

            val payload = this.body()!!
            actionRepository.saveAccessToken(payload.token)
        }

        return@runBlocking true
    }

    private fun CustomRequestServletWrapper.toRequestBody(): String {

        val payload = StringBuilder()

        return try {
            val inputStream = this.inputStream
            val br = BufferedReader(InputStreamReader(inputStream))
            while (true) {
                val line = br.readLine() ?: break
                payload.append(line)
            }

            payload.toString()
        } catch (exception: Exception) {
            throw RuntimeException("ActionRequest 변환 발생 : ${exception.message}")
        }
    }
}
