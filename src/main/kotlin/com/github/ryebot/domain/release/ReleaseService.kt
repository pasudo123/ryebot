package com.github.ryebot.domain.release

import com.github.ryebot.api.model.TriggerRequest
import com.github.ryebot.constant.Branch.RELEASE
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.client.throwApiException
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.awaitResponse

@Service
class ReleaseService(
    private val githubApiClient: GithubApiClient
) {

    private val releaseVersionBodyRegEx = Regex("^!Release\\sVersion\\s\\d+\\.\\d+\\.\\d+")
    private val log = LoggerFactory.getLogger(javaClass)

    fun releaseIfPossibleOrNot(triggerRequest: TriggerRequest) = runBlocking {

        if (isReleaseBaseBranch(triggerRequest)) {
            log.warn("base 브랜치가 release 가 아닙니다.")
            return@runBlocking
        }

        if (triggerRequest.userComment.isBlank()) {
            log.warn("사용자 코멘트가 없습니다.")
            return@runBlocking
        }

        val userComment = triggerRequest.userComment
        val matchResult = releaseVersionBodyRegEx.matchEntire(userComment)

        if (matchResult == null) {
            log.warn("사용자 코멘트가 잘못 입력되었습니다. : $userComment")
            return@runBlocking
        }
    }

    /**
     * base 브랜치가 release 여부 확인
     */
    private suspend fun isReleaseBaseBranch(triggerRequest: TriggerRequest): Boolean {

        val response = githubApiClient.getPr(
            triggerRequest.owner,
            triggerRequest.repositoryName,
            triggerRequest.prNumber,
        ).awaitResponse()

        if (response.isSuccessful.not()) {
            response.throwApiException("풀 리퀘스트 조회를 실패하였습니다.")
            return false
        }


        return response.body()?.base?.ref?.contains(RELEASE) ?: false
    }
}