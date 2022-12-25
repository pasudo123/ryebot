package com.github.ryebot.infra.client.error

/**
 * https://docs.github.com/en/rest/overview/resources-in-the-rest-api?apiVersion=2022-11-28#client-errors
 */
data class GithubErrorResponse(
    val message: String? = null,
    val errors: List<ErrorDetail> = emptyList(),
    val documentationUrl: String? = null
) {

    data class ErrorDetail(
        val resources: String,
        val field: String,
        val code: String
    )
}