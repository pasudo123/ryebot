package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "comment 정보", reference = "")
data class CommentDto(
    val id: Long?,
    val body: String?,
)
