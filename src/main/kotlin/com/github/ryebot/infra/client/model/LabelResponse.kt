package com.github.ryebot.infra.client.model

data class LabelResponse(
    val id: Long,
    val nodeId: String,
    val url: String,
    val name: String,
    val description: String?,
    val color: String?,
    val default: Boolean?
)
