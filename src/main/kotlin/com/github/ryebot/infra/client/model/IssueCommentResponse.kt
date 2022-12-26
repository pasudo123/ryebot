package com.github.ryebot.infra.client.model

data class IssueCommentResponse(
    val id: Long,
    val nodeId: String,
    val url: String,
    val body: String,
    val createdAt: String,
    val updatedAt: String,
    val issueUrl: String,
)
