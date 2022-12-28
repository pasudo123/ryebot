package com.github.ryebot.infra.client.model

data class CheckRunResponses(
    val totalCount: Long,
    val checkRuns: List<CheckRunResponse> = emptyList()
)
