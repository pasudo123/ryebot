package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "head commit 정보")
data class HeadDto(
    val label: String,
    val ref: String,
    val sha: String,
    val user: GithubUserDto,
    val repo: RepositoryDto,
)
