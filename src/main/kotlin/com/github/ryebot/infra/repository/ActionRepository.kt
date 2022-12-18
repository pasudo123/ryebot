package com.github.ryebot.infra.repository

import com.github.ryebot.infra.repository.model.GitHubToken
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class ActionRepository(
    private val redisTemplate: StringRedisTemplate
) {

    fun saveToken(
        gitHubToken: GitHubToken,
        expireAt: Long = 3
    ) {
        val key = "${TOKEN}:${gitHubToken.installationId}"

        with(redisTemplate) {
            this.boundValueOps(key).set(gitHubToken.token!!)
            this.expire(key, Duration.ofHours(expireAt))
        }
    }

    fun getTokenOrNull(
        gitHubToken: GitHubToken
    ): String? {
        val key = "${TOKEN}:${gitHubToken.installationId}"
        return redisTemplate.boundValueOps(key).get()
    }

    companion object {
        private const val TOKEN = "token:install"
    }
}
