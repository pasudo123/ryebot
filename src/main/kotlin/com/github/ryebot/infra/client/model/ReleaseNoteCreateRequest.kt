package com.github.ryebot.infra.client.model

data class ReleaseNoteCreateRequest(
    val tagName: String,
    val targetCommitsh: String,
)
