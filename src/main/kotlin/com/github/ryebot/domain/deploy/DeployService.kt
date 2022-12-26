package com.github.ryebot.domain.deploy

import com.github.ryebot.domain.deploy.model.DeployParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.throwApiException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class DeployService(
    private val githubApiClient: GithubApiClient,
    private val deployMergeService: DeployMergeService,
    private val deployReleaseService: DeployReleaseService,
    private val deployLabelService: DeployLabelService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun releaseIfPossibleOrNot(deployParam: DeployParam) {
        if (deployParam.deployPossible().not()) {
            return
        }
    }

    private suspend fun DeployParam.deployPossible(): Boolean {
        if (this.isReleaseBaseBranch().not()) {
            log.warn("base 브랜치가 release 가 아닙니다.")
            return false
        }

        if (this.isUserCommentEmpty()) {
            log.warn("사용자 코멘트가 없습니다.")
            return false
        }

        if (this.isReleaseCommentRegExWrong()) {
            log.warn("사용자 코멘트가 잘못되었습니다. : ${this.userComment}")
            notifyToUserWrongComment(this, message = "릴리즈 코멘트를 잘못 입력하였습니다.\n다시 입력해주세요.")
            return false
        }

        if (this.isCommentVersionMatchTitleVersion().not()) {
            log.warn("사용자 코멘트와 릴리즈 타이틀의 버전이 서로 불일치 합니다.")
            notifyToUserWrongComment(this, message = "코멘트와 타이틀의 버전이 서로 불일치 합니다.\n다시 입력해주세요.")
            return false
        }

        return true
    }

    suspend fun notifyToUserWrongComment(deployParam: DeployParam, message: String) {

        val response = githubApiClient.createIssueComment(
            deployParam.repository.owner,
            deployParam.repository.name,
            deployParam.pullRequest.prNumber,
            IssueCommentRequest(body = message)
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("릴리즈 코멘트 노티를 실패했습니다.")
        }
    }
}