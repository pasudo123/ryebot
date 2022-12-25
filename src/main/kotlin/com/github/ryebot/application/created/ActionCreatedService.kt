package com.github.ryebot.application.created

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.release.ReleaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ActionCreatedService(
    private val releaseService: ReleaseService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {
        releaseService.releaseIfPossibleOrNot(triggerRequest)
    }
}
