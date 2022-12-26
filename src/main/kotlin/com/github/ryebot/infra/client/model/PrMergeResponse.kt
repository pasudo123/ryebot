package com.github.ryebot.infra.client.model

data class PrMergeResponse(
    val sha: String,
    val merged: Boolean,
    val message: String
)
