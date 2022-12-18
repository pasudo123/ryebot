package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "label 정보", reference = "https://docs.github.com/en/rest/pulls/pulls?apiVersion=2022-11-28#get-a-pull-request")
data class LabelDto(
    val id: Long,
    val nodeId: String,
    val url: String,
    val name: String,
    val description: String,
    val color: String,
    val default: Boolean
)
