package com.github.ryebot.domain.release

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.error.ApiException
import com.github.ryebot.error.ErrorCode
import com.github.ryebot.error.ErrorWrapper
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.getErrorBody
import com.github.ryebot.infra.client.model.ReleaseResponse
import com.github.ryebot.infra.repository.ActionRepository
import com.github.ryebot.infra.repository.model.ReleaseVo
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class ReleaseService(
    private val githubApiClient: GithubApiClient,
    private val actionRepository: ActionRepository
) {

    suspend fun getLatestVersion(triggerRequest: TriggerRequest): ReleaseResponse {

        val response = githubApiClient.getLatestRelease(
            triggerRequest.owner,
            triggerRequest.repositoryName
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            throw ApiException(
                ErrorWrapper(
                    message = "최신 버전 릴리즈를 조회하지 못했습니다. : ${response.getErrorBody()}",
                    ErrorCode.API001
                )
            )
        }

        return response.body()!!
    }

    suspend fun saveLatestVersion(triggerRequest: TriggerRequest, releaseResponse: ReleaseResponse) {
        val releaseVo = ReleaseVo(
            owner = triggerRequest.owner,
            repoName = triggerRequest.repositoryName,
            major = releaseResponse.major,
            minor = releaseResponse.minor,
            patch = releaseResponse.patch
        )
        actionRepository.saveCommits()
    }
}
