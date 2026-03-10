package com.p10.stats.client

import com.p10.stats.client.domain.GpResultInfo
import com.p10.stats.config.OPEN_F1_CLIENT
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux

@Component
class OpenF1SessionResultClient(
    @Qualifier(OPEN_F1_CLIENT) private val webClient: WebClient,
) {
    fun getClassification(sessionKey: Int): List<GpResultInfo> =
        webClient
            .get()
            .uri("/session_result") { uriBuilder ->
                uriBuilder
                    .queryParam("session_key", sessionKey)
                    .build()
            }.retrieve()
            .bodyToFlux<GpResultInfo>()
            .collectList()
            .block() ?: throw Exception("Classification data not found for this race!")
}
