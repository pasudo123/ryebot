package com.github.ryebot.api.model

import com.github.ryebot.api.model.detail.GithubUserDto
import com.github.ryebot.api.model.detail.InstallationDto
import com.github.ryebot.api.model.detail.PullRequestDto
import com.github.ryebot.api.model.detail.RepositoryDto
import io.swagger.annotations.ApiModel

@ApiModel(
    value = "웹훅 페이로드",
    reference = "https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads"
)
data class TriggerRequest(
    val action: String,
    val number: Int,
    val installation: InstallationDto,
    val pullRequest: PullRequestDto,
    val repository: RepositoryDto,
    val sender: GithubUserDto,
)
