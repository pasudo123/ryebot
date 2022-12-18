package com.github.ryebot.api.model.detail

data class HeadDto(
    val label: String,
    val ref: String,
    val sha: String,
    val user: GithubUserDto,
    val repo: RepositoryDto,
)