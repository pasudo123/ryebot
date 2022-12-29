package com.github.ryebot.application.model

import com.github.ryebot.domain.deploy.model.DeployBranchParam
import com.github.ryebot.domain.deploy.model.DeployPrepareParam
import com.github.ryebot.domain.pullrequest.model.PullRequest
import com.github.ryebot.domain.pullrequest.model.PullRequestGetParam

data class WebHookPayload(
    val action: String?,
    val installationId: Long?,
    val baseBranch: String?,
    val owner: String?,
    val repositoryName: String?,
    val prNumber: Long?,
    val userComment: String?,
    val commitSha: String?,
    val isBotSender: Boolean?
) {

    fun toPullRequestGetParam(): PullRequestGetParam {
        return PullRequestGetParam(
            this.owner!!,
            this.repositoryName!!,
            this.prNumber!!
        )
    }

    fun toDeployBranchParamWithPullRequest(pullRequest: PullRequest): DeployBranchParam {
        return DeployBranchParam(
            DeployBranchParam.PullRequest(
                prNumber = pullRequest.number,
                title = pullRequest.title,
                body = pullRequest.body,
                baseBranch = pullRequest.baseBranch,
            ),
            DeployBranchParam.Repository(
                owner = this.owner!!,
                name = this.repositoryName!!
            ),
            this.userComment!!,
            this.isBotSender!!
        )
    }

    fun toDeployPrepareParam(): DeployPrepareParam {
        return DeployPrepareParam(
            baseBranch = this.baseBranch!!,
            owner = this.owner!!,
            repositoryName = this.repositoryName!!,
            prNumber = this.prNumber!!,
            commitSha = this.commitSha!!
        )
    }
}
