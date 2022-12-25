package com.github.ryebot.infra.client.model

data class CommitResponse(
    val commit: Commit
) {

    data class Commit(
        val message: String
    )
}
