package com.github.ryebot.infra.client.model

data class ReleaseCreateRequest(
    val name: String,
    val body: String,
    val tagName: String,
    val targetCommitish: String,
)
