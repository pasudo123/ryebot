package com.github.ryebot.application

import com.github.ryebot.application.action.ActionCreatedService
import com.github.ryebot.application.action.ActionOpenService
import com.github.ryebot.application.action.ActionSyncService
import com.github.ryebot.application.model.WebHookPayload
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class WebHookService(
    private val actionOpenService: ActionOpenService,
    private val actionCreatedService: ActionCreatedService,
    private val actionSyncService: ActionSyncService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    fun process(webHookPayload: WebHookPayload) = runBlocking {
        val currentAction = WebHookEventType.getCurrentActionOrNull(webHookPayload.action!!) ?: return@runBlocking

        when (currentAction) {
            WebHookEventType.REOPENED,
            WebHookEventType.OPENED -> { actionOpenService.doAction(webHookPayload) }
            WebHookEventType.CREATED -> { actionCreatedService.doAction(webHookPayload) }
            WebHookEventType.SYNCHRONIZE -> { actionSyncService.doAction(webHookPayload) }
            else -> {
                log.warn("작동하지 않는 액션이 들어왔습니다. : action[$currentAction]")
                return@runBlocking
            }
        }
    }
}
