package com.github.ryebot.domain.deploy.detail

import com.github.ryebot.config.mapper.toObject
import com.github.ryebot.domain.deploy.model.DeployPrepareParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.error.GithubErrorResponse
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.model.ReleaseResponse
import com.github.ryebot.infra.client.throwApiException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class DeployPrepareCommentService(
    private val githubApiClient: GithubApiClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun prepareReleaseVersionComment(deployPrepareParam: DeployPrepareParam) {

        if (deployPrepareParam.isBaseBranchRelease().not()) {
            log.warn("해당 브랜치[${deployPrepareParam.baseBranch}]는 릴리즈 버저닝 코멘트를 생성할 수 없습니다.")
            return
        }

        val releaseResponse = deployPrepareParam.getLatestRelease()
        deployPrepareParam.appendCommentWithVersioning(releaseResponse)
    }

    private suspend fun DeployPrepareParam.appendCommentWithVersioning(releaseResponse: ReleaseResponse) {
        val response = githubApiClient.createIssueComment(
            this.owner,
            this.repositoryName,
            this.prNumber,
            IssueCommentRequest(body = releaseResponse.preReleaseComment)
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("릴리즈 및 버저닝 코멘트를 작성하지 못했습니다.")
        }
    }

    private suspend fun DeployPrepareParam.getLatestRelease(): ReleaseResponse {
        val response = githubApiClient.getLatestRelease(
            this.owner,
            this.repositoryName
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
}
