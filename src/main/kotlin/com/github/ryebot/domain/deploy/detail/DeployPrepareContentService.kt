package com.github.ryebot.domain.deploy.detail

import com.github.ryebot.domain.deploy.model.DeployPrepareParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.PrUpdateRequest
import com.github.ryebot.infra.client.throwApiException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class DeployPrepareContentService(
    private val githubApiClient: GithubApiClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun updateTitleAndContentOnRelease(deployPrepareParam: DeployPrepareParam) {

        if (deployPrepareParam.isBaseBranchRelease().not()) {
            log.warn("해당 브랜치[${deployPrepareParam.baseBranch}]는 릴리즈 타이틀 및 내용을 수정할 수 없습니다.")
            return
        }

        val prUpdateRequest = PrUpdateRequest(
            title = deployPrepareParam.releaseTitleOrEmpty(),
            body = deployPrepareParam.getCommitMessages(),
            base = deployPrepareParam.baseBranch
        )

        val response = githubApiClient.updatePr(
            deployPrepareParam.owner,
            deployPrepareParam.repositoryName,
            deployPrepareParam.prNumber,
            prUpdateRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("pr 타이틀 및 콘텐츠 수정을 실패하였습니다.")
        }
    }

    private suspend fun DeployPrepareParam.getCommitMessages(): String {

        val commitResponses = githubApiClient.getCommitsByPr(
            this.owner,
            this.repositoryName,
            this.prNumber,
        ).awaitResponse()

        if (commitResponses.isSuccessful.not()) {
            commitResponses.throwApiException("커밋 메시지를 조회할 수 없습니다.")
        }

        val bodyBuilder = StringBuilder().apply {
            this.appendLine("## Release 내용")
        }

        commitResponses.body()?.forEach { commitResponse ->
            bodyBuilder.appendLine(commitResponse.commit.message)
        }

        return bodyBuilder.toString()
    }
}
