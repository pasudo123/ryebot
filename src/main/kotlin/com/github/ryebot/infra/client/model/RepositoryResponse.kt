package com.github.ryebot.infra.client.model

data class RepositoryResponse(
    val id: Long,
    val nodeId: String,
    val name: String,
    val fullName: String,
    val owner: GithubUserResponse,
    val htmlUrl: String,
    val description: String,
    val fork: Boolean,
    val createdAt: String,
    val updatedAt: String
)
