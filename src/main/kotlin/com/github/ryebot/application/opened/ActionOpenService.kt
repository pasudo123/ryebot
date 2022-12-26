package com.github.ryebot.application.opened

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.comment.CommentService
import com.github.ryebot.domain.pullrequest.PullRequestContentService
import com.github.ryebot.domain.release.ReleaseGetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionOpenService(
    private val pullRequestContentService: PullRequestContentService,
    private val releaseGetService: ReleaseGetService,
    private val commentService: CommentService
) {

    fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {
        listOf(
            async(Dispatchers.IO) {
                pullRequestContentService.changeTitleAndContentIfRelease(triggerRequest)
            },
            async(Dispatchers.IO) {
                val releaseResponse = releaseGetService.getLatestVersion(triggerRequest)
                commentService.appendIssueComment(triggerRequest, releaseResponse)
            }
        ).awaitAll()
    }
}
