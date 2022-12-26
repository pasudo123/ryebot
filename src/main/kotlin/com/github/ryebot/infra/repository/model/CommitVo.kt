package com.github.ryebot.infra.repository.model

import com.github.ryebot.api.model.TriggerRequest

data class CommitVo(
    val owner: String,
    val repository: String,
    val prNumber: Long,
    val body: String? = null
) {

    companion object {
        val EMPTY = CommitVo(
            owner = "",
            repository = "",
            prNumber = -1L
        )

        fun from(request: TriggerRequest, paramBody: String? = null): CommitVo {
            return CommitVo(
                owner = request.owner,
                repository = request.repositoryName,
                prNumber = request.prNumber,
                body = paramBody
            )
        }
    }
}
