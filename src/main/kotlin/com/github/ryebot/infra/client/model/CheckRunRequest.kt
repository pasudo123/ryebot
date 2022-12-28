package com.github.ryebot.infra.client.model

data class CheckRunRequest(
    val name: String,
    val status: String,
    val startedAt: String? = null,
    val completedAt: String? = null,
    val headSha: String? = null,
    val checkRunId: Long? = null,
    val conclusion: String? = null,
)
