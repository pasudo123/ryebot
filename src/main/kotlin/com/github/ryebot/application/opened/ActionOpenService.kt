package com.github.ryebot.application.opened

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.domain.comment.CommentService
import com.github.ryebot.domain.pullrequest.PullRequestContentService
import com.github.ryebot.domain.release.ReleaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class ActionOpenService(
    private val pullRequestContentService: PullRequestContentService,
    private val releaseService: ReleaseService,
    private val commentService: CommentService
) {

    fun doAction(triggerRequest: TriggerRequest) = runBlocking(Dispatchers.IO) {
        listOf(
            async(Dispatchers.IO) {
                pullRequestContentService.changeTitleAndContentIfRelease(triggerRequest)
            },
            async(Dispatchers.IO) {
                val releaseResponse = releaseService.getLatestVersion(triggerRequest)
                with(releaseResponse) {
                    releaseService.saveLatestVersion(triggerRequest, this)
                    commentService.appendIssueComment(triggerRequest, this)
                }
            }
        ).awaitAll()
    }
}
