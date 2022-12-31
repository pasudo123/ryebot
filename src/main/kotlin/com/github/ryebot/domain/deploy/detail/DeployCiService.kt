package com.github.ryebot.domain.deploy.detail

import com.github.ryebot.domain.deploy.model.DeployCheckParam
import com.github.ryebot.domain.deploy.model.DeployPrepareParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.getErrorBody
import com.github.ryebot.infra.client.model.CheckRunRequest
import com.github.ryebot.infra.client.throwApiException
import com.github.ryebot.util.DateUtil.toISO8601Format
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse
import java.time.LocalDateTime

@Service
class DeployCiService(
    private val githubApiClient: GithubApiClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun progressDeployCheck(deployPrepareParam: DeployPrepareParam): DeployCheckParam? {
        val checkRunRequest = CheckRunRequest(
            name = RYEBOT_DEPLOY,
            status = "in_progress",
            headSha = deployPrepareParam.commitSha,
            startedAt = LocalDateTime.now().toISO8601Format(),
        )

        val response = githubApiClient.createCheckRun(
            deployPrepareParam.owner,
            deployPrepareParam.repositoryName,
            checkRunRequest
        ).awaitResponse()

        if (response.isSuccessful) {
            val checkRunResponse = response.body()!!
            return DeployCheckParam(
                checkRunResponse.id,
                checkRunResponse.headSha
            )
        }

        response.throwApiException("deploy check run 생성에 실패했습니다.")
        return null
    }

    suspend fun completedDeployCheck(deployPrepareParam: DeployPrepareParam, deployCheckParam: DeployCheckParam) {

        val checkRunRequest = CheckRunRequest(
            name = RYEBOT_DEPLOY,
            status = "completed",
            checkRunId = deployCheckParam.id,
            completedAt = LocalDateTime.now().toISO8601Format(),
            conclusion = "success"
        )

        val response = githubApiClient.updateCheckRun(
            deployPrepareParam.owner,
            deployPrepareParam.repositoryName,
            checkRunId = deployCheckParam.id,
            checkRunRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            log.error("check run update 에 실패했습니다. : ${response.getErrorBody()}")
        }
    }

    fun sample() {
    }

    companion object {
        private const val RYEBOT_DEPLOY = "ryebot-deploy"
    }
}
