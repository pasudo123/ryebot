package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "레파지토리 속성", reference = "https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#get-a-repository")
data class RepositoryDto(
    val id: Long?,
    val nodeId: String?,
    val name: String?,
    val fullName: String?,
    val owner: GithubUserDto?,
    val htmlUrl: String?,
    val description: String?,
    val fork: Boolean?,
    val createdAt: String?,
    val updatedAt: String?
)
