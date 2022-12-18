package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "base commit 정보")
data class BaseDto(
    val label: String,
    val ref: String,
    val sha: String,
    val user: GithubUserDto,
    val repo: RepositoryDto,
)
