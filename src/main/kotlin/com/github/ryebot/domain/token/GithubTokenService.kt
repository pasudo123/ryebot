package com.github.ryebot.domain.token

import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.repository.ActionRepository
import com.github.ryebot.infra.repository.model.GitHubToken
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class GithubTokenService(
    private val githubApiClient: GithubApiClient,
    private val actionRepository: ActionRepository
) {

    fun saveTokenIfNull(installationId: Long) = runBlocking {
        val token = actionRepository.getTokenOrNull(GitHubToken(installationId))

        if (token?.isNotBlank() == true) {
            return@runBlocking
        }

        val githubApiResponse = githubApiClient
            .createTokenByInstallId(installationId)
            .awaitResponse()

        if (githubApiResponse.isSuccessful) {
            val payload = githubApiResponse.body()!!
            actionRepository.saveToken(GitHubToken(installationId, payload.token))
        }
    }
}