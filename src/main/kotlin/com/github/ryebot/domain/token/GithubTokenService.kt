package com.github.ryebot.domain.token

import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.throwApiException
import com.github.ryebot.infra.repository.ActionRepository
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class GithubTokenService(
    private val githubApiClient: GithubApiClient,
    private val actionRepository: ActionRepository
) {

    fun saveTokenIfNull(installationId: Long) = runBlocking {
        val token = actionRepository.getTokenOrNull()

        if (token?.isNotBlank() == true) {
            return@runBlocking
        }

        val response = githubApiClient
            .createTokenByInstallId(installationId)
            .awaitResponse()

        if (response.isSuccessful) {
            val payload = response.body()!!
            actionRepository.saveToken(payload.token)
        }

        response.throwApiException("github token 획득 실패")
    }
}
