package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "Issue request 정보")
data class IssueDto(
    val title: String?,
    val number: Long?,
)
