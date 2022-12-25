package com.github.ryebot.domain.pullrequest

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.constant.Branch.RELEASE
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.PrUpdateRequest
import com.github.ryebot.infra.client.throwApiException
import com.github.ryebot.infra.repository.ActionRepository
import com.github.ryebot.infra.repository.model.CommitVo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class PullRequestContentService(
    private val githubApiClient: GithubApiClient,
    private val actionRepository: ActionRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun changeTitleAndContentIfRelease(triggerRequest: TriggerRequest) {
        if (triggerRequest.baseBranch.contains(RELEASE).not()) {
            log.warn("해당 브랜치[${triggerRequest.baseBranch}]는 타이틀과 내용을 수정할 수 없습니다.")
            return
        }

        val prUpdateRequest = PrUpdateRequest(
            title = triggerRequest.releaseTitleOrEmpty(),
            body = triggerRequest.getCommitMessages(),
            base = triggerRequest.baseBranch
        )

        val response = githubApiClient.updatePr(
            triggerRequest.owner,
            triggerRequest.repositoryName,
            triggerRequest.prNumber,
            prUpdateRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("pr 타이틀 및 콘텐츠 수정을 실패하였습니다.")
        }
    }

    private suspend fun TriggerRequest.getCommitMessages(): String {
        if (this.pullRequest == null ||
            this.baseBranch.contains(RELEASE).not()
        ) {
            return ""
        }

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

        val releaseBodyBuilder = StringBuilder()

        commitResponses.body()?.forEach { commitResponse ->
            bodyBuilder.appendLine(commitResponse.commit.message)
            releaseBodyBuilder.appendLine(commitResponse.commit.message)
        }

        // release note 에 작성될 내용
        actionRepository.saveCommits(CommitVo.from(this, releaseBodyBuilder.toString()))

        return bodyBuilder.toString()
    }
}
