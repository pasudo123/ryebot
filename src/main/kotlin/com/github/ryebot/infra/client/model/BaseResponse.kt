package com.github.ryebot.infra.client.model

data class BaseResponse(
    val label: String?,
    val ref: String?,
    val sha: String?,
    val user: GithubUserResponse,
    val repo: RepositoryResponse
)