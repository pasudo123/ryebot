package com.github.ryebot.infra.repository

import com.github.ryebot.config.mapper.toJson
import com.github.ryebot.infra.repository.model.CommitVo
import com.github.ryebot.infra.repository.model.ReleaseVo
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class ActionRepository(
    private val redisTemplate: StringRedisTemplate
) {

    fun saveToken(
        token: String,
        expireAt: Long = 3
    ) {
        with(redisTemplate) {
            this.boundValueOps(TOKEN).set(token)
            this.expire(TOKEN, Duration.ofHours(expireAt))
        }
    }

    fun getTokenOrNull(): String? {
        return redisTemplate.boundValueOps(TOKEN).get()
    }

    fun saveCommits(commitVo: CommitVo) {
        val key = "$COMMIT:${commitVo.owner}:${commitVo.repository}:${commitVo.prNumber}"
        with(redisTemplate) {
            this.boundValueOps(key).set(commitVo.toJson())
            this.expire(key, Duration.ofHours(240))
        }
    }

    fun saveRelease(releaseVo: ReleaseVo) {
        val key = "$RELEASE:${releaseVo.owner}:${releaseVo.repository}"
        with(redisTemplate) {
            this.boundValueOps(key).set(releaseVo.toJson())
            this.expire(key, Duration.ofHours(240))
        }
    }

    companion object {
        private const val TOKEN = "access-token"
        private const val COMMIT = "commit"
        private const val RELEASE = "release"
    }
}
