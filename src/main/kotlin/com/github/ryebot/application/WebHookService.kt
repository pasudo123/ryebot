package com.github.ryebot.application

import com.github.ryebot.api.model.TriggerRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class WebHookService {

    @Async
    fun action(triggerRequest: TriggerRequest) {

    }
}