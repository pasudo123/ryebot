package com.github.ryebot.infra.repository.model

data class GitHubToken(
    val installationId: Long,
    val token: String? = null
)