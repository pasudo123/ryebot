package com.github.ryebot.api.model

import com.github.ryebot.api.model.detail.GithubUserDto
import com.github.ryebot.api.model.detail.InstallationDto
import com.github.ryebot.api.model.detail.IssueDto
import com.github.ryebot.api.model.detail.PullRequestDto
import com.github.ryebot.api.model.detail.RepositoryDto
import com.github.ryebot.constant.Branch
import io.swagger.annotations.ApiModel

@ApiModel(
    value = "웹훅 페이로드",
    reference = "https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads"
)
data class TriggerRequest(
    val action: String,
    val installation: InstallationDto,
    val repository: RepositoryDto,
    val sender: GithubUserDto,
    val number: Long? = null,
    val issue: IssueDto? = null,
    val pullRequest: PullRequestDto? = null,
) {

    val githubAppInstallationId = installation.id
    val baseBranch = this.pullRequest?.base?.ref ?: ""
    val owner = this.repository.owner.login
    val repositoryName = this.repository.name
    val prNumber = this.number ?: this.issue?.number!!

    fun releaseTitleOrEmpty(): String {
        if (this.baseBranch.contains(Branch.RELEASE)) {
            return "\uD83D\uDE80 Release version x.y.z"
        }

        return ""
    }
}
