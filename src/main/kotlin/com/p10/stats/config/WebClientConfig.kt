package com.p10.stats.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

const val OPEN_F1_CLIENT = "openF1Client"

@Configuration
class WebClientConfig(
    @Value("\${openF1.url}") private val openF1url: String,
) {
    @Bean
    @Qualifier(OPEN_F1_CLIENT)
    fun openF1Client(): WebClient = WebClient.create(openF1url)
}
