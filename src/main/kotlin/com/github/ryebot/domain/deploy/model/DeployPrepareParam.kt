package com.github.ryebot.domain.deploy.model

import com.github.ryebot.constant.Branch.RELEASE

data class DeployPrepareParam(
    val baseBranch: String,
    val owner: String,
    val repositoryName: String,
    val prNumber: Long
) {

    fun isBaseBranchRelease(): Boolean {
        return this.baseBranch.contains(RELEASE)
    }

    fun releaseTitleOrEmpty(): String {
        if (this.baseBranch.contains(RELEASE)) {
            return "\uD83D\uDE80 Release version x.y.z"
        }

        return ""
    }
}
