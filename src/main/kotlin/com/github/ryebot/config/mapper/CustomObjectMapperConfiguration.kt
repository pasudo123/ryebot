package com.github.ryebot.config.mapper

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.ryebot.config.mapper.ObjectMapperConfiguration.Companion.mapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return mapper
    }

    companion object {
        val mapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // not null 만 처리.
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            // snake case 대응 : @JsonProperty("commit_message")
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    }
}

inline fun <reified T : Any> T.toJson(): String = mapper.writeValueAsString(this)
inline fun <reified T : Any> String.toObject(): T = mapper.readValue(this, T::class.java)
inline fun <reified T : Any> String.toList(): List<T> = mapper.readValue(this, object : TypeReference<List<T>>() {})
