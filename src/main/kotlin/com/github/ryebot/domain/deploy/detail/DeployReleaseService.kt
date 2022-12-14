package com.github.ryebot.domain.deploy.detail

import com.github.ryebot.constant.Branch.RELEASE
import com.github.ryebot.domain.deploy.model.DeployBranchParam
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.model.LabelRequest
import com.github.ryebot.infra.client.model.MergeBranchRequest
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

    fun release(deployBranchParam: DeployBranchParam) = runBlocking(Dispatchers.IO) {
        listOf(
            async { deployBranchParam.setLabel() },
            async { deployBranchParam.createRelease() },
            async { deployBranchParam.createReleaseNote() },
            async { deployBranchParam.mergeReleaseToStandardBranch() }
        ).awaitAll()

        // 릴리즈 완료에 대한 코멘트 작성
        deployBranchParam.createReleaseComment()
    }

    private suspend fun DeployBranchParam.setLabel() {
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

    private suspend fun DeployBranchParam.createRelease() {

        val releaseCreateRequest = ReleaseCreateRequest(
            name = "Release ${this.getVersionTag()}",
            body = this.pullRequest.body,
            tagName = this.getVersionTag(),
            targetCommitish = this.pullRequest.baseBranch
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

    private suspend fun DeployBranchParam.createReleaseNote() {

        val releaseNoteCreateRequest = ReleaseNoteCreateRequest(
            tagName = this.getVersionTag(),
            targetCommitsh = this.pullRequest.baseBranch,
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

    private suspend fun DeployBranchParam.createReleaseComment() {

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
            IssueCommentRequest(
                body = """
                ## Release Note
                ${currentRelease?.htmlUrl}
                """.trimIndent()
            )
        ).awaitResponse()

        if (commentResponse.isSuccessful.not()) {
            commentResponse.throwApiException("릴리즈 완료 코멘트 작성에 실패했습니다.")
        }
    }

    /**
     * 기준브랜치로 다시 병합처리.
     * release -> (master | main) 로 병합한다.
     */
    private suspend fun DeployBranchParam.mergeReleaseToStandardBranch() {
        val baseBranch = "main"

        // base branch 가 이젠 head 가 되어서 머지한다.
        val headBranch = this.pullRequest.baseBranch

        val mergeBranchRequest = MergeBranchRequest(
            base = baseBranch,
            head = headBranch,
            commitMessage = "\uD83E\uDD16 AUTO MERGE :: $headBranch -> $baseBranch"
        )

        val response = githubApiClient.mergeBranch(
            this.repository.owner,
            this.repository.name,
            mergeBranchRequest
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("릴리즈 브랜치 머지에 실패했습니다.")
        }
    }
}
