package com.github.ryebot.infra.client.model

data class ReleaseCreateResponse(
    val id: Long,
    val tagName: String,
    val targetCommitish: String,
    val body: String
)
