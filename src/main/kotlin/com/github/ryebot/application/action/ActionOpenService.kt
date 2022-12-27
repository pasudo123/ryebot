package com.github.ryebot.application.action

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.deploy.DeployPrepareService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionOpenService(
    private val deployPrepareService: DeployPrepareService
) : ActionService {

    override fun doAction(triggerRequest: TriggerRequest): Unit = runBlocking(Dispatchers.IO) {
        deployPrepareService.prepareIfReleaseBranch(triggerRequest.toDeployPrepareParam())
    }
}
