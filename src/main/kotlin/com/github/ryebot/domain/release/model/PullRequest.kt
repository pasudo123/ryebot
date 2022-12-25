package com.github.ryebot.domain.release.model

class PullRequest(
    val id: Long,
    val number: Long,
    val title: String,
    val body: String,
    val base: BaseBranch
) {
}