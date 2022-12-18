package com.github.ryebot.infra.client

import com.github.ryebot.infra.client.model.installation.InstallationResponse
import retrofit2.Call
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
}
