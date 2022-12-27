package com.github.ryebot.application

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.application.action.ActionCreatedService
import com.github.ryebot.application.action.ActionOpenService
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class WebHookService(
    private val actionOpenService: ActionOpenService,
    private val actionCreatedService: ActionCreatedService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    fun process(triggerRequest: TriggerRequest) = runBlocking {
        val currentAction = WebHookEventType.getCurrentActionOrNull(triggerRequest.action) ?: return@runBlocking

        when (currentAction) {
            WebHookEventType.OPENED -> { actionOpenService.doAction(triggerRequest) }
            WebHookEventType.CREATED -> { actionCreatedService.doAction(triggerRequest) }
            else -> {
                log.warn("작동하지 않는 액션이 들어왔습니다. : action[$currentAction]")
                return@runBlocking
            }
        }
    }
}
