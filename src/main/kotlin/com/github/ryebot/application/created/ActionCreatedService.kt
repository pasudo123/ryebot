package com.github.ryebot.application.created

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.deploy.DeployService
import com.github.ryebot.domain.deploy.model.DeployParam
import com.github.ryebot.domain.pullrequest.PullRequestGetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ActionCreatedService(
    private val pullRequestGetService: PullRequestGetService,
    private val deployService: DeployService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {

        val currentPullRequest = pullRequestGetService.getPrBy(triggerRequest.toPullRequestGetParam()) ?: return@runBlocking

        val deployParam = DeployParam(
            DeployParam.PullRequest(
                prNumber = currentPullRequest.number,
                title = currentPullRequest.title,
                body = currentPullRequest.body,
                baseBranch = currentPullRequest.baseBranch
            ),
            DeployParam.Repository(
                owner = triggerRequest.owner,
                name = triggerRequest.repositoryName
            ),
            triggerRequest.userComment,
            triggerRequest.isSenderTypeBot()
        )

        deployService.releaseIfPossibleOrNot(deployParam)
    }
}
