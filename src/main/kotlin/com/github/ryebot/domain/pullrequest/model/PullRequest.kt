package com.github.ryebot.domain.pullrequest.model

class PullRequest(
    val id: Long,
    val number: Long,
    val title: String,
    val body: String,
    val baseBranch: String
)