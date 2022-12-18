package com.github.ryebot.application

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.application.opened.ActionOpenService
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class WebHookService(
    private val actionOpenService: ActionOpenService
) {

    @Async
    fun process(triggerRequest: TriggerRequest) = runBlocking {
        val currentAction = WebHookEventType.getCurrentActionOrNull(triggerRequest.action) ?: return@runBlocking

        when (currentAction) {
            WebHookEventType.OPENED -> { actionOpenService.doAction(triggerRequest) }
        }
    }
}
