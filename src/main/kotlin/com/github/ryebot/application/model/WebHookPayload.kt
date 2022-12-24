package com.github.ryebot.application.model

data class WebHookPayload(
    val installationId: Long?,
    val baseBranch: String?,
    val owner: String?,
    val repositoryName: String?,
    val prNumber: Long?
)
