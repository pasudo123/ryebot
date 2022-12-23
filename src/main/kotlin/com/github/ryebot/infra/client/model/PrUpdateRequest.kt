package com.github.ryebot.infra.client.model

data class PrUpdateRequest(
    val title: String,
    val base: String,
    val body: String = "",
    val state: String = "open"
)
