package com.github.ryebot.application.opened

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.pullrequest.PullRequestContentService
import org.springframework.stereotype.Service

@Service
class ActionOpenService(
    pullRequestContentService: PullRequestContentService
) {

    fun doAction(triggerRequest: TriggerRequest) {
    }
}
