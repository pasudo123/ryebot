package com.github.ryebot.api.model

import com.github.ryebot.api.model.detail.CommentDto
import com.github.ryebot.api.model.detail.GithubUserDto
import com.github.ryebot.api.model.detail.InstallationDto
import com.github.ryebot.api.model.detail.IssueDto
import com.github.ryebot.api.model.detail.PullRequestDto
import com.github.ryebot.api.model.detail.RepositoryDto
import com.github.ryebot.application.model.WebHookPayload
import com.github.ryebot.constant.Branch
import com.github.ryebot.domain.deploy.model.DeployBranchParam
import com.github.ryebot.domain.deploy.model.DeployPrepareParam
import com.github.ryebot.domain.pullrequest.model.PullRequest
import com.github.ryebot.domain.pullrequest.model.PullRequestGetParam
import io.swagger.annotations.ApiModel

@ApiModel(
    value = "웹훅 페이로드",
    reference = "https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads"
)
data class TriggerRequest(
    val action: String = "empty",
    val installation: InstallationDto? = null,
    val repository: RepositoryDto? = null,
    val sender: GithubUserDto? = null,
    val number: Long? = null,
    val issue: IssueDto? = null,
    val pullRequest: PullRequestDto? = null,
    val comment: CommentDto? = null,
) {

    val githubAppInstallationId = installation?.id
    val baseBranch = this.pullRequest?.base?.ref ?: "empty"
    val owner = this.repository?.owner?.login ?: "empty"
    val repositoryName = this.repository?.name ?: "empty"
    val prNumber = this.number ?: this.issue?.number!!
    val userComment = this.comment?.body ?: ""

    fun isSenderTypeBot(): Boolean {
        return sender?.type == "Bot"
    }

    fun releaseTitleOrEmpty(): String {
        if (this.baseBranch.contains(Branch.RELEASE)) {
            return "\uD83D\uDE80 Release version x.y.z"
        }

        return ""
    }

    fun toWebHookPayload(): WebHookPayload {
        return WebHookPayload(
            installationId = githubAppInstallationId,
            baseBranch = baseBranch,
            owner = owner,
            repositoryName = repositoryName,
            prNumber = prNumber
        )
    }

    fun toPullRequestGetParam(): PullRequestGetParam {
        return PullRequestGetParam(
            this.owner,
            this.repositoryName,
            this.prNumber
        )
    }

    fun toDeployBranchParamWithPullRequest(PullRequest: PullRequest): DeployBranchParam {
        return DeployBranchParam(
            DeployBranchParam.PullRequest(
                prNumber = PullRequest.number,
                title = PullRequest.title,
                body = PullRequest.body,
                baseBranch = PullRequest.baseBranch
            ),
            DeployBranchParam.Repository(
                owner = this.owner,
                name = this.repositoryName
            ),
            this.userComment,
            this.isSenderTypeBot()
        )
    }

    fun toDeployPrepareParam(): DeployPrepareParam {
        return DeployPrepareParam(
            baseBranch = this.baseBranch,
            owner = this.owner,
            repositoryName = this.repositoryName,
            prNumber = this.prNumber
        )
    }
}
