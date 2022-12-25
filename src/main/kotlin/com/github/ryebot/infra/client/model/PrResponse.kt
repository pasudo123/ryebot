package com.github.ryebot.infra.client.model

data class PrResponse(
    val url: String,
    val id: Long,
    val number: Long,
    val state: String,
    val title: String,
    val body: String,
    val base: BaseResponse,
    val head: HeadResponse
)
