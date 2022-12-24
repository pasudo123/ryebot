package com.github.ryebot.infra.repository.model

data class ReleaseVo(
    val owner: String,
    val repoName: String,
    val major: Long,
    val minor: Long,
    val patch: Long
)
