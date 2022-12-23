package com.github.ryebot.api

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.application.WebHookService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("trigger")
class WebHookController(
    private val webHookService: WebHookService
) {

    @PostMapping
    fun trigger(@RequestBody triggerRequest: TriggerRequest) {
        webHookService.process(triggerRequest)
    }
}

