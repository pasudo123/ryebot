package com.github.ryebot.domain.pullrequest.model

data class PullRequestGetParam(
    val owner: String,
    val repositoryName: String,
    val number: Long
)