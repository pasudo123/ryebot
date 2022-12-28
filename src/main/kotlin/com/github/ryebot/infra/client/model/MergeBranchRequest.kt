package com.github.ryebot.infra.client.model

data class MergeBranchRequest(
    val base: String,
    val head: String,
    val commitMessage: String? = null
)
