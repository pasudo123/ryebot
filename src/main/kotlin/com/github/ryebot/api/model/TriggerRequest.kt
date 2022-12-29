package com.github.ryebot.api.model

import com.github.ryebot.api.model.detail.CommentDto
import com.github.ryebot.api.model.detail.GithubUserDto
import com.github.ryebot.api.model.detail.InstallationDto
import com.github.ryebot.api.model.detail.IssueDto
import com.github.ryebot.api.model.detail.PullRequestDto
import com.github.ryebot.api.model.detail.RepositoryDto
import com.github.ryebot.application.model.WebHookPayload
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

    private val commitSha = this.pullRequest?.head?.sha ?: "empty"
    private val repositoryName = this.repository?.name ?: "empty"
    private val prNumber = this.number ?: this.issue?.number!!
    private val userComment = this.comment?.body ?: ""
    private val isSenderBotType = this.sender?.type == "Bot"

    val githubAppInstallationId = installation?.id
    val baseBranch = this.pullRequest?.base?.ref ?: "empty"
    val owner = this.repository?.owner?.login ?: "empty"

    fun toWebHookPayload(): WebHookPayload {
        return WebHookPayload(
            action = action,
            installationId = githubAppInstallationId,
            baseBranch = baseBranch,
            owner = owner,
            repositoryName = repositoryName,
            prNumber = prNumber,
            userComment = userComment,
            commitSha = commitSha,
            isBotSender = this.isSenderBotType
        )
    }
}
