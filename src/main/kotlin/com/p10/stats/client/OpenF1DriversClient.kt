package com.p10.stats.client

import com.p10.stats.client.domain.GpDriverInfo
import com.p10.stats.config.OPEN_F1_CLIENT
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux

@Component
class OpenF1DriversClient(
    @Qualifier(OPEN_F1_CLIENT) private val webClient: WebClient,
) {
    fun getDriversData(sessionKey: Int): List<GpDriverInfo>? =
        webClient
            .get()
            .uri("/drivers") { uriBuilder ->
                uriBuilder
                    .queryParam("session_key", sessionKey)
                    .build()
            }.retrieve()
            .bodyToFlux<GpDriverInfo>()
            .collectList()
            .block()
}
