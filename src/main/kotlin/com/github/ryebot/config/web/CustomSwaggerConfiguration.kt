package com.github.ryebot.config.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Configuration
@EnableSwagger2
class CustomSwaggerConfiguration {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            // 타입 string($date-time) 을 string 으로 바꿈.
            .directModelSubstitute(LocalDate::class.java, String::class.java)
            .directModelSubstitute(LocalDateTime::class.java, String::class.java)
            .directModelSubstitute(LocalTime::class.java, String::class.java)
            .apiInfo(apiInfo())
            .groupName("ryebot")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.github.ryebot"))
            // paths 는 모두 등록
            .paths(PathSelectors.any())
            .build()
    }

    fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("RYEBOT 프로젝트")
            .description("github webhook")
            .version("1.0")
            .build()
    }
}
