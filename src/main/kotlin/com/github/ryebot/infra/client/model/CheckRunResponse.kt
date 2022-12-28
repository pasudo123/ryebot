package com.github.ryebot.infra.client.model

class CheckRunResponse(
    val id: Long,
    val headSha: String,
    val nodeId: String,
    val url: String,
    val htmlUrl: String,
    val status: String,
)
