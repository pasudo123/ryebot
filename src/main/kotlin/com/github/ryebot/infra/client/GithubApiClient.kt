package com.github.ryebot.infra.client

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface GithubApiClient {

    /**
     * https://docs.github.com/en/developers/apps/building-github-apps/authenticating-with-github-apps#authenticating-as-an-installation
     */
    @POST("app/installations/{installId}/access_tokens")
    fun createTokenByInstallId(
        @Path("installId") installId: Long
    ): Call<>
}
