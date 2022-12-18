package com.github.ryebot.config.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ryebot.config.web.filter.CustomOncePerFilterForCache
import com.github.ryebot.config.web.interceptor.CustomGithubTokenInterceptor
import com.github.ryebot.config.web.interceptor.CustomLoggingInterceptor
import com.github.ryebot.domain.token.GithubTokenService
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.repository.ActionRepository
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class CustomWebConfiguration(
    private val mapper: ObjectMapper,
    private val githubTokenService: GithubTokenService
) : WebMvcConfigurer {

    /**
     * swagger-ui -> swagger-ui/index.html 로 리다이렉트 한다.
     */
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html")
    }
    /**
     * swagger-ui 를 리소스로 등록한다.
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    /**
     * 인터셉터 추가
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.apply {
            this.addInterceptor(CustomLoggingInterceptor(mapper))
                .order(INTERCEPTOR_ORDER_1)
            this.addInterceptor(CustomGithubTokenInterceptor(githubTokenService))
                .addPathPatterns(listOf("/trigger/*"))
                .order(INTERCEPTOR_ORDER_2)
        }
    }

    /**
     * 디스패처 서블릿 앞단 필터 추가
     */
    @Bean
    fun cacheOncePerFilter(): FilterRegistrationBean<CustomOncePerFilterForCache> {
        return FilterRegistrationBean<CustomOncePerFilterForCache>().apply {
            this.filter = CustomOncePerFilterForCache()
            this.order = 1
        }
    }

    companion object {
        private const val INTERCEPTOR_ORDER_1 = 1
        private const val INTERCEPTOR_ORDER_2 = 2
    }
}
