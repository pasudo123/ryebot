package com.github.ryebot.domain.deploy.detail

import com.github.ryebot.domain.deploy.model.DeployBranchParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.PrMergeRequest
import com.github.ryebot.infra.client.throwApiException
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class DeployMergeService(
    private val githubApiClient: GithubApiClient
) {

    suspend fun merge(deployBranchParam: DeployBranchParam): Boolean = runBlocking {

        val mergeRequest = PrMergeRequest()

        val response = githubApiClient.mergePr(
            deployBranchParam.repository.owner,
            deployBranchParam.repository.name,
            deployBranchParam.pullRequest.prNumber,
            mergeRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("머지에 실패했습니다.")
            return@runBlocking false
        }

        val mergeResponse = response.body()
        return@runBlocking mergeResponse?.merged ?: false
    }
}
