package com.github.ryebot.infra.client.model.installation

import com.github.ryebot.infra.client.model.RepositoryResponse

data class InstallationResponse(
    val token: String,
    val expiredAt: String,
    val permissions: Permissions,
    val repositorySelection: String,
    val repositories: List<RepositoryResponse> = emptyList()
) {

    data class Permissions(
        val issues: String,
        val contents: String
    )
}
