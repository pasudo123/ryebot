package com.github.ryebot.application.opened

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.pullrequest.PullRequestContentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionOpenService(
    private val pullRequestContentService: PullRequestContentService
) {

    fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {

        pullRequestContentService.changeTitleAndContentIfRelease(triggerRequest)
    }
}
