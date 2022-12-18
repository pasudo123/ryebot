package com.github.ryebot.config.client

import com.github.ryebot.infra.repository.ActionRepository
import com.github.ryebot.util.Jwt
import okhttp3.Interceptor
import okhttp3.Response

class CustomNetworkInterceptor(
    private val actionRepository: ActionRepository
) : Interceptor {

    private val jwtRequiredPaths = listOf("app/installations")

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val requiredJwtToken = jwtRequiredPaths.find { path -> request.url.encodedPath.contains(path) }

        val builder = request.newBuilder()

        builder.addHeader("Accept", "application/vnd.github+json")
        if (requiredJwtToken != null) {
            val jwt = Jwt.build()
            builder.addHeader("Authorization", "Bearer $jwt")
        } else {
            builder.addHeader("Authorization", "Bearer ${actionRepository.getTokenOrNull()}")
        }

        return chain.proceed(builder.build())
    }
}
