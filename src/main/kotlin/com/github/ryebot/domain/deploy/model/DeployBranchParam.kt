package com.github.ryebot.domain.deploy.model

import com.github.ryebot.constant.Branch.RELEASE

data class DeployBranchParam(
    val pullRequest: PullRequest,
    val repository: Repository,
    val userComment: String,
    val isSenderBot: Boolean
) {

    data class PullRequest(
        val prNumber: Long,
        val title: String,
        val body: String,
        val baseBranch: String,
        val commitSha: String,
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
        releaseRegEx.matchEntire(this.userComment) ?: return true
        return false
    }

    fun isCommentVersionMatchTitleVersion(): Boolean {
        val userCommentVersion = versionRegEx.find(this.userComment)?.groupValues?.first()
        val titleVersion = versionRegEx.find(this.pullRequest.title)?.groupValues?.first()

        if (userCommentVersion == null || titleVersion == null) {
            return false
        }

        return userCommentVersion == titleVersion
    }

    fun getVersionTag(): String {
        val releaseElements = this.userComment.split(" ")

        // v1.2.3
        return "v${releaseElements[2]}"
    }

    companion object {
        private val releaseRegEx = Regex("^!Release\\sVersion\\s\\d+\\.\\d+\\.\\d+")
        private val versionRegEx = Regex("\\d+\\.\\d+\\.\\d")
    }
}
