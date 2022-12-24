package com.github.ryebot.api.model.detail

import io.swagger.annotations.ApiModel

@ApiModel(value = "pull request 정보", reference = "https://docs.github.com/en/rest/pulls/pulls?apiVersion=2022-11-28#get-a-pull-request")
data class PullRequestDto(
    val url: String?,
    val id: Long?,
    val nodeId: String?,
    val htmlUrl: String?,
    val diffUrl: String?,
    val patchUrl: String?,
    val issueUrl: String?,
    val commitsUrl: String?,
    val reviewCommentsUrl: String?,
    val reviewCommentUrl: String?,
    val commentsUrl: String?,
    val statusesUrl: String?,
    val number: Long?,
    val state: String?,
    val locked: Boolean?,
    val user: GithubUserDto?,
    val body: String?,
    val labels: List<LabelDto> = emptyList(),
    val createdAt: String?,
    val updatedAt: String?,
    val closedAt: String?,
    val mergedAt: String?,
    val mergeCommitSha: String?,
    val assignee: GithubUserDto?,
    val assignees: List<GithubUserDto> = emptyList(),
    val requestedReviewers: List<GithubUserDto> = emptyList(),
    val head: HeadDto?,
    val base: BaseDto?,
    val comments: Long?,
    val reviewComments: Long?,
    val maintainerCanModify: Boolean?,
    val commits: Long?,
    val additions: Long?,
    val deletions: Long?,
    val changedFiles: Long?
)
