package com.github.ryebot.application.action

import com.github.ryebot.application.model.WebHookPayload

interface ActionService {

    fun doAction(webHookPayload: WebHookPayload)
}
