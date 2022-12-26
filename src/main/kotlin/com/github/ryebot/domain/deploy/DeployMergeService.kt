package com.github.ryebot.domain.deploy

import com.github.ryebot.domain.deploy.model.DeployParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.PrMergeRequest
import com.github.ryebot.infra.client.throwApiException
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class DeployMergeService(
    private val githubApiClient: GithubApiClient
) {

    suspend fun merge(deployParam: DeployParam): Boolean {

        val mergeRequest = PrMergeRequest()

        val response = githubApiClient.mergePr(
            deployParam.repository.owner,
            deployParam.repository.name,
            deployParam.pullRequest.prNumber,
            mergeRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("머지에 실패했습니다.")
            return false
        }

        val mergeResponse = response.body()
        return mergeResponse?.merged ?: false
    }
}
