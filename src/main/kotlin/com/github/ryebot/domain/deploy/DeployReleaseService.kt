package com.github.ryebot.domain.deploy

import com.github.ryebot.constant.Branch.RELEASE
import com.github.ryebot.domain.deploy.model.DeployParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.model.LabelRequest
import com.github.ryebot.infra.client.model.ReleaseCreateRequest
import com.github.ryebot.infra.client.model.ReleaseNoteCreateRequest
import com.github.ryebot.infra.client.throwApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class DeployReleaseService(
    private val githubApiClient: GithubApiClient
) {

    fun release(deployParam: DeployParam) = runBlocking(Dispatchers.IO) {
        listOf(
            async { deployParam.setLabel() },
            async { deployParam.createRelease() },
            async { deployParam.createReleaseNote() },
        ).awaitAll()

        // 릴리즈 완료에 대한 코멘트 작성
        deployParam.createReleaseComment()
    }

    private suspend fun DeployParam.setLabel() {
        val response = githubApiClient.setLabels(
            this.repository.owner,
            this.repository.name,
            this.pullRequest.prNumber,
            LabelRequest(labels = listOf(RELEASE, this.getVersionTag()))
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("레이블 설정에 실패했습니다.")
        }
    }

    private suspend fun DeployParam.createRelease() {

        val releaseCreateRequest = ReleaseCreateRequest(
            name = "Release ${this.getVersionTag()}",
            body = this.pullRequest.body,
            tagName = this.getVersionTag(),
            targetCommitish = RELEASE
        )

        val response = githubApiClient.createRelease(
            this.repository.owner,
            this.repository.name,
            releaseCreateRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("릴리즈 생성에 실패했습니다.")
        }
    }

    private suspend fun DeployParam.createReleaseNote() {

        val releaseNoteCreateRequest = ReleaseNoteCreateRequest(
            tagName = this.getVersionTag(),
            targetCommitsh = RELEASE,
        )

        val response = githubApiClient.createReleaseNote(
            this.repository.owner,
            this.repository.name,
            releaseNoteCreateRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("릴리즈노트 생성에 실패했습니다.")
        }
    }

    private suspend fun DeployParam.createReleaseComment() {

        val releaseResponse = githubApiClient.getLatestRelease(
            this.repository.owner,
            this.repository.name,
        ).awaitResponse()

        if (releaseResponse.isSuccessful.not()) {
            releaseResponse.throwApiException("최근 릴리즈 조회에 실패하였습니다.")
        }

        val currentRelease = releaseResponse.body()

        val commentResponse = githubApiClient.createIssueComment(
            this.repository.owner,
            this.repository.name,
            this.pullRequest.prNumber,
            IssueCommentRequest(body = """
                ## Release Note
                ${currentRelease?.htmlUrl}
            """.trimIndent())
        ).awaitResponse()

        if (commentResponse.isSuccessful.not()) {
            commentResponse.throwApiException("릴리즈 완료 코멘트 작성에 실패했습니다.")
        }
    }
}
