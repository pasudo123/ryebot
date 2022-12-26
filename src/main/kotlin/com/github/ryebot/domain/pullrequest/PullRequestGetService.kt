package com.github.ryebot.domain.pullrequest

import com.github.ryebot.domain.pullrequest.model.BaseBranch
import com.github.ryebot.domain.pullrequest.model.PullRequest
import com.github.ryebot.domain.pullrequest.model.PullRequestGetParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.throwApiException
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class PullRequestGetService(
    private val githubApiClient: GithubApiClient
) {

    suspend fun getPrBy(getParam: PullRequestGetParam): PullRequest? {
        val response = githubApiClient.getPr(
            getParam.owner,
            getParam.repositoryName,
            getParam.number,
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("풀 리퀘스트 조회를 실패하였습니다.")
            return null
        }

        response.body()?.let { prResponse ->
            return PullRequest(
                id = prResponse.id,
                number = prResponse.number,
                title = prResponse.title,
                body = prResponse.body,
                baseBranch = prResponse.base.ref ?: ""
            )
        }

        return null
    }
}