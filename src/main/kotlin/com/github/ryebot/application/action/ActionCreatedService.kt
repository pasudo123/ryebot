package com.github.ryebot.application.action

import com.github.ryebot.api.model.TriggerRequest
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

    override fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {
        val currentPullRequest = pullRequestGetService.getPrBy(triggerRequest.toPullRequestGetParam()) ?: return@runBlocking
        deployBranchService.releaseIfPossibleOrNot(triggerRequest.toDeployBranchParamWithPullRequest(currentPullRequest))
    }
}
