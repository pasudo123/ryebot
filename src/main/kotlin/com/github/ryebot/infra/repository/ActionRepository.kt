package com.github.ryebot.infra.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class ActionRepository(
    private val redisTemplate: StringRedisTemplate
) {

    fun saveToken(token: String, expireAt: Long = 3) {
        with(redisTemplate) {
            this.boundValueOps(TOKEN).set(token)
            this.expire(TOKEN, Duration.ofHours(expireAt))
        }
    }

    fun getTokenOrNull(): String? {
        return redisTemplate.boundValueOps(TOKEN).get()
    }

    companion object {
        private const val TOKEN = "token"
    }
}
