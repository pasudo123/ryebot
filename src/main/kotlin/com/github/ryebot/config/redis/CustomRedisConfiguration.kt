package com.github.ryebot.config.redis

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class CustomRedisConfiguration(
    private val redisProperties: RedisProperties
) {

    fun connectionFactory(database: Int): LettuceConnectionFactory {
        return LettuceConnectionFactory(redisProperties.host, redisProperties.port).apply {
            this.database = database
        }
    }

    @Bean
    fun redisTemplate(): StringRedisTemplate {
        return StringRedisTemplate().apply {
            this.setConnectionFactory(connectionFactory(database = DEFAULT_DB))
        }
    }

    companion object {
        private const val DEFAULT_DB = 0
    }
}