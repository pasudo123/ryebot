package com.github.ryebot.domain.deploy

import com.github.ryebot.infra.client.GithubApiClient
import org.springframework.stereotype.Service

@Service
class DeployReleaseService(
    private val githubApiClient: GithubApiClient
) {
}