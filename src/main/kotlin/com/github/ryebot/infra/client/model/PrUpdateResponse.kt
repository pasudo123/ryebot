package com.github.ryebot.infra.client.model

data class PrUpdateResponse(
    val url: String,
    val id: Long,
    val number: Long,
    val state: String,
    val title: String,
)
