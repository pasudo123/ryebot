package com.github.ryebot.infra.client

import com.github.ryebot.infra.client.model.CheckRunRequest
import com.github.ryebot.infra.client.model.CheckRunResponse
import com.github.ryebot.infra.client.model.CheckRunResponses
import com.github.ryebot.infra.client.model.CommitResponse
import com.github.ryebot.infra.client.model.IssueCommentRequest
import com.github.ryebot.infra.client.model.IssueCommentResponse
import com.github.ryebot.infra.client.model.LabelRequest
import com.github.ryebot.infra.client.model.LabelResponse
import com.github.ryebot.infra.client.model.PrMergeRequest
import com.github.ryebot.infra.client.model.PrMergeResponse
import com.github.ryebot.infra.client.model.PrResponse
import com.github.ryebot.infra.client.model.PrUpdateRequest
import com.github.ryebot.infra.client.model.ReleaseCreateRequest
import com.github.ryebot.infra.client.model.ReleaseCreateResponse
import com.github.ryebot.infra.client.model.ReleaseNoteCreateRequest
import com.github.ryebot.infra.client.model.ReleaseNoteCreateResponse
import com.github.ryebot.infra.client.model.ReleaseResponse
import com.github.ryebot.infra.client.model.installation.InstallationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("repos/{owner}/{repo}/releases/generate-notes")
    fun createReleaseNote(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body releaseNoteCreateRequest: ReleaseNoteCreateRequest
    ): Call<ReleaseNoteCreateResponse>

    /**
     * https://docs.github.com/en/rest/releases/releases?apiVersion=2022-11-28#create-a-release
     */
    @POST("repos/{owner}/{repo}/releases")
    fun createRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body releaseCreateRequest: ReleaseCreateRequest
    ): Call<ReleaseCreateResponse>

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

    /**
     * https://docs.github.com/en/rest/pulls/pulls?apiVersion=2022-11-28#merge-a-pull-request
     */
    @PUT("repos/{owner}/{repo}/pulls/{pullNumber}/merge")
    fun mergePr(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("pullNumber") pullNumber: Long,
        @Body prMergeRequest: PrMergeRequest,
    ): Call<PrMergeResponse>

    /**
     * https://docs.github.com/en/rest/issues/labels#set-labels-for-an-issue
     */
    @PUT("repos/{owner}/{repo}/issues/{issueNumber}/labels")
    fun setLabels(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") pullNumber: Long,
        @Body labelUpdateRequest: LabelRequest
    ): Call<List<LabelResponse>>

    @POST("repos/{owner}/{repo}/check-runs")
    fun createCheckRun(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body checkRunRequest: CheckRunRequest
    ): Call<CheckRunResponse>

    @PATCH("repos/{owner}/{repo}/check-runs/{checkRunId}")
    fun updateCheckRun(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("checkRunId") checkRunId: Long,
        @Body checkRunRequest: CheckRunRequest
    ): Call<CheckRunResponse>
}
