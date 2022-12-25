package com.github.ryebot.domain.comment

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.constant.Branch
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.model.ReleaseResponse
import com.github.ryebot.infra.client.throwApiException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class CommentService(
    private val githubApiClient: GithubApiClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun appendIssueComment(
        triggerRequest: TriggerRequest,
        releaseResponse: ReleaseResponse
    ) {

        if (triggerRequest.baseBranch.contains(Branch.RELEASE).not()) {
            log.warn("해당 브랜치[${triggerRequest.baseBranch}]는 릴리즈 코멘트를 자동으로 생성할 수 없습니다.")
            return
        }

        val response = githubApiClient.createIssueComment(
            triggerRequest.owner,
            triggerRequest.repositoryName,
            triggerRequest.prNumber,
            IssueCommentRequest(body = releaseResponse.preReleaseComment)
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("issue comment 을 작성하지 못했습니다.")
        }
    }
}
