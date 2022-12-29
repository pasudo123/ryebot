package com.github.ryebot.application.action

import com.github.ryebot.application.model.WebHookPayload
import com.github.ryebot.domain.deploy.DeployPrepareService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionOpenService(
    private val deployPrepareService: DeployPrepareService
) : ActionService {

    override fun doAction(webHookPayload: WebHookPayload): Unit = runBlocking(Dispatchers.IO) {
        deployPrepareService.prepareIfReleaseBranch(webHookPayload.toDeployPrepareParam())
    }
}
