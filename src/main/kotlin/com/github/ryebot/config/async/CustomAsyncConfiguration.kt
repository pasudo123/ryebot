package com.github.ryebot.config.async

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class CustomAsyncConfiguration : AsyncConfigurerSupport() {

    override fun getAsyncExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            this.corePoolSize = 4
            this.maxPoolSize = 10
            this.queueCapacity = Integer.MAX_VALUE
            this.setThreadNamePrefix("ryebot-async-")
            this.setWaitForTasksToCompleteOnShutdown(true)
            this.setAwaitTerminationMillis(5000L)
            this.initialize()
        }
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return CustomAsyncExceptionHandler()
    }
}
