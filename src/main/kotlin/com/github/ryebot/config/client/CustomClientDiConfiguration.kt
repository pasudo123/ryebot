package com.github.ryebot.config.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ryebot.infra.client.GithubApiClient
import com.github.ryebot.infra.repository.ActionRepository
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Configuration
@ConfigurationProperties(prefix = "client")
class CustomClientDiConfiguration(
    private val mapper: ObjectMapper,
    private val actionRepository: ActionRepository
) {

    class Client(
        var host: String? = null,
        var connectionTimeout: Long = 3000,
        var readTimeout: Long = 3000,
        var writeTimeout: Long = 3000,
    )

    val github = Client()

    private val headers = Headers.headersOf(namesAndValues = arrayOf("Content-Type", "application/json"))

    @Bean
    fun githubApiClient(): GithubApiClient {

        return Retrofit.Builder()
            .baseUrl(github.host!!)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .callFactory(httpClient(github))
            .build()
            .create(GithubApiClient::class.java)
    }

    private fun httpClient(client: Client): OkHttpClient {
        val customNetworkInterceptor = CustomNetworkInterceptor(actionRepository)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .connectTimeout(client.connectionTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(client.readTimeout, TimeUnit.MILLISECONDS)
            .writeTimeout(client.writeTimeout, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(customNetworkInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder().headers(headers)
                val request = builder.build()
                chain.proceed(request)
            }.build()
    }
}
