package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "github app 설치정보", reference = "https://docs.github.com/en/enterprise-server@3.4/rest/apps/apps#get-an-installation-for-the-authenticated-app")
data class InstallationDto(
    val id: Long,
    val account: GithubUserDto,
    val accessTokensUrl: String,
    val repositoriesUrl: String,
    val htmlUrl: String,
    val appId: Long,
    val targetId: Long,
    val targetType: String,
    val permissions: PermissionsDto,
    val events: List<String> = emptyList(),
    val createdAt: String,
    val updatedAt: String
) {

    data class PermissionsDto(
        val checks: String,
        val metadata: String,
        val contents: String
    )
}