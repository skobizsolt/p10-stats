package com.p10.stats.client

import com.p10.stats.config.OPEN_F1_CLIENT
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import com.p10.stats.repository.domain.GpBaseDetails

@Component
class OpenF1SessionsClient(
    @Qualifier(OPEN_F1_CLIENT) private val webClient: WebClient,
) {
    fun getGpsBasicDatailsForYear(year: Int): List<GpBaseDetails> =
        webClient
            .get()
            .uri("/sessions") { uriBuilder ->
                uriBuilder
                    .queryParam("year", year)
                    .queryParam("session_type", "Sprint", "Race")
                    .build()
            }.retrieve()
            .bodyToFlux<GpBaseDetails>()
            .collectList()
            .block()
            .orEmpty()
            .filterNotNull()
}
