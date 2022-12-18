package com.github.ryebot.config.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class CustomWebConfiguration : WebMvcConfigurer {

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
}
