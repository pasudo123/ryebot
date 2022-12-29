package com.github.ryebot.application.action

import com.github.ryebot.application.model.WebHookPayload
import com.github.ryebot.domain.deploy.DeployBranchService
import com.github.ryebot.domain.pullrequest.PullRequestGetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionCreatedService(
    private val pullRequestGetService: PullRequestGetService,
    private val deployBranchService: DeployBranchService,
) : ActionService {

    override fun doAction(webHookPayload: WebHookPayload) = runBlocking(Dispatchers.IO) {
        val currentPullRequest = pullRequestGetService.getPrBy(webHookPayload.toPullRequestGetParam()) ?: return@runBlocking
        deployBranchService.releaseIfPossibleOrNot(webHookPayload.toDeployBranchParamWithPullRequest(currentPullRequest))
    }
}
