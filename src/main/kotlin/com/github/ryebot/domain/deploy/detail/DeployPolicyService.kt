package com.github.ryebot.domain.deploy.detail

import com.github.ryebot.domain.deploy.model.DeployBranchParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.throwApiException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class DeployPolicyService(
    private val githubApiClient: GithubApiClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun possible(deployBranchParam: DeployBranchParam): Boolean {
        if (deployBranchParam.isSenderBot) {
            log.warn("봇이 작성한 메시지는 배포 정책을 수행하지 않습니다.")
            return false
        }

        if (deployBranchParam.isReleaseBaseBranch().not()) {
            log.warn("base 브랜치가 release 가 아닙니다.")
            return false
        }

        if (deployBranchParam.isUserCommentEmpty()) {
            log.warn("사용자 코멘트가 없습니다.")
            return false
        }

        if (deployBranchParam.isReleaseCommentRegExWrong()) {
            log.warn("사용자 코멘트가 잘못되었습니다. : ${deployBranchParam.userComment}")
            notifyToUserWrongComment(deployBranchParam, message = "릴리즈 코멘트를 잘못 입력하였습니다.\n다시 입력해주세요.")
            return false
        }

        if (deployBranchParam.isCommentVersionMatchTitleVersion().not()) {
            log.warn("사용자 코멘트와 릴리즈 타이틀의 버전이 서로 불일치 합니다.")
            notifyToUserWrongComment(deployBranchParam, message = "코멘트와 타이틀의 버전이 서로 불일치 합니다.\n다시 입력해주세요.")
            return false
        }

        return true
    }

    private suspend fun notifyToUserWrongComment(deployBranchParam: DeployBranchParam, message: String) {

        val response = githubApiClient.createIssueComment(
            deployBranchParam.repository.owner,
            deployBranchParam.repository.name,
            deployBranchParam.pullRequest.prNumber,
            IssueCommentRequest(body = message)
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("릴리즈 코멘트 노티를 실패했습니다.")
        }
    }
}
