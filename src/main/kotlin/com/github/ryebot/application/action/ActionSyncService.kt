package com.github.ryebot.application.action

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.deploy.detail.DeployPrepareContentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionSyncService(
    private val deployPrepareContentService: DeployPrepareContentService
) : ActionService {

    override fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {
        deployPrepareContentService.updateTitleAndContentOnRelease(triggerRequest.toDeployPrepareParam())
    }
}
