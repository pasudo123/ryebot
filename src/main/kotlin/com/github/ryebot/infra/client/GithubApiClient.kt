package com.github.ryebot.infra.client

import com.github.ryebot.infra.client.model.CommitResponse
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.model.IssueCommentResponse
import com.github.ryebot.infra.client.model.PrUpdateRequest
import com.github.ryebot.infra.client.model.PrResponse
import com.github.ryebot.infra.client.model.ReleaseResponse
import com.github.ryebot.infra.client.model.installation.InstallationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GithubApiClient {

    /**
     * https://docs.github.com/en/developers/apps/building-github-apps/authenticating-with-github-apps#authenticating-as-an-installation
     * https://docs.github.com/en/rest/apps/apps?apiVersion=2022-11-28#create-an-installation-access-token-for-an-app
     */
    @POST("app/installations/{installId}/access_tokens")
    fun createTokenByInstallId(
        @Path("installId") installId: Long
    ): Call<InstallationResponse>

    /**
     * https://docs.github.com/en/rest/pulls/pulls#list-commits-on-a-pull-request
     */
    @GET("repos/{owner}/{repo}/pulls/{pullNumber}/commits")
    fun getCommitsByPr(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pullNumber") pullNumber: Long,
    ): Call<List<CommitResponse>>

    /**
     * https://docs.github.com/en/rest/pulls/pulls#update-a-pull-request
     */
    @PATCH("repos/{owner}/{repo}/pulls/{pullNumber}")
    fun updatePr(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pullNumber") pullNumber: Long,
        @Body prUpdateRequest: PrUpdateRequest
    ): Call<PrResponse>

    /**
     * https://docs.github.com/en/rest/releases/releases?generate-release-notes-content-for-a-release=&apiVersion=2022-11-28#get-the-latest-release
     */
    @GET("repos/{owner}/{repo}/releases/latest")
    fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<ReleaseResponse>

    /**
     * https://docs.github.com/en/rest/issues/comments?apiVersion=2022-11-28#create-an-issue-comment
     */
    @POST("repos/{owner}/{repo}/issues/{issueNumber}/comments")
    fun createIssueComment(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Long,
        @Body issueCommentRequest: IssueCommentRequest
    ): Call<IssueCommentResponse>

    /**
     * https://docs.github.com/en/rest/pulls/pulls?apiVersion=2022-11-28#get-a-pull-request
     */
    @GET("repos/{owner}/{repo}/pulls/{pullNumber}")
    fun getPr(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pullNumber") pullNumber: Long,
    ): Call<PrResponse>
}
