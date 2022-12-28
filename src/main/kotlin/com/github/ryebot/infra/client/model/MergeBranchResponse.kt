package com.github.ryebot.infra.client.model

data class MergeBranchResponse(
    val url: String,
    val sha: String,
    val htmlUrl: String,
    val commentsUrl: String,
)
