package com.github.ryebot.domain.release

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.config.mapper.toObject
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.error.GithubErrorResponse
import com.github.ryebot.infra.client.model.ReleaseResponse
import com.github.ryebot.infra.client.throwApiException
import com.github.ryebot.infra.repository.ActionRepository
import com.github.ryebot.infra.repository.model.ReleaseVo
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class ReleaseGetService(
    private val githubApiClient: GithubApiClient,
    private val actionRepository: ActionRepository
) {

    suspend fun getLatestVersion(triggerRequest: TriggerRequest): ReleaseResponse {

        val response = githubApiClient.getLatestRelease(
            triggerRequest.owner,
            triggerRequest.repositoryName
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            val errorResponse = response.errorBody()?.string()?.toObject<GithubErrorResponse>()

            if (errorResponse?.isFirstRelease() == true) {
                return ReleaseResponse.FIRST_RELEASE
            }

            response.throwApiException("최신 버전 릴리즈를 조회하지 못했습니다.")
        }

        return response.body()!!
    }

    /**
     * 처음 release 하는 경우
     */
    private fun GithubErrorResponse.isFirstRelease(): Boolean {
        return this.message == "Not Found"
    }

    suspend fun saveLatestVersion(triggerRequest: TriggerRequest, releaseResponse: ReleaseResponse) {

        val releaseVo = ReleaseVo(
            owner = triggerRequest.owner,
            repository = triggerRequest.repositoryName,
            major = releaseResponse.major,
            minor = releaseResponse.minor,
            patch = releaseResponse.patch
        )

        actionRepository.saveRelease(releaseVo)
    }
}
