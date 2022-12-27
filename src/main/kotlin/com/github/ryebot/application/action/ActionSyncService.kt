package com.github.ryebot.application.action

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.pullrequest.PullRequestContentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionSyncService(
    private val pullRequestContentService: PullRequestContentService,
): ActionService {

    override fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {
        pullRequestContentService.changeTitleAndContentIfRelease(triggerRequest)
    }
}