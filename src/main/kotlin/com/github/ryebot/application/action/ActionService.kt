package com.github.ryebot.application.action

import com.github.ryebot.api.model.TriggerRequest

interface ActionService {

    fun doAction(triggerRequest: TriggerRequest)
}
