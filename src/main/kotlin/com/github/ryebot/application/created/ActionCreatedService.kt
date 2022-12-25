package com.github.ryebot.application.created

import com.github.ryebot.api.model.TriggerRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ActionCreatedService() {

    private val log = LoggerFactory.getLogger(javaClass)

    fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {
    }
}
