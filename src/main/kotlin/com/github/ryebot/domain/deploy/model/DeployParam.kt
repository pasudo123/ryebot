package com.github.ryebot.domain.deploy.model

import com.github.ryebot.constant.Branch.RELEASE
import org.intellij.lang.annotations.RegExp

data class DeployParam(
    val pullRequest: PullRequest,
    val repository: Repository,
    val userComment: String
) {

    data class PullRequest(
        val prNumber: Long,
        val title: String,
        val body: String,
        val baseBranch: String
    )

    data class Repository(
        val owner: String,
        val name: String,
    )

    fun isReleaseBaseBranch(): Boolean {
        return this.pullRequest.baseBranch.contains(RELEASE)
    }

    fun isUserCommentEmpty(): Boolean {
        return this.userComment.isBlank()
    }

    fun isReleaseCommentRegExWrong(): Boolean {
        releaseRegEx.matchEntire(this.userComment) ?: return false
        return true
    }

    fun isCommentVersionMatchTitleVersion(): Boolean {
        return this.userComment.replace(notNumberRegEx, "") == this.pullRequest.title.replace(notNumberRegEx, "")
    }

    companion object {
        private val releaseRegEx = Regex("^!Release\\sVersion\\s\\d+\\.\\d+\\.\\d+")
        private val notNumberRegEx = Regex("\\D")
    }
}