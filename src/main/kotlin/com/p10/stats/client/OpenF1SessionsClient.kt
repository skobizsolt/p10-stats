package com.p10.stats.client

import com.p10.stats.client.domain.GpBaseDetails
import com.p10.stats.client.domain.GpGridInfo
import com.p10.stats.client.domain.GpTrackScheduleDetails
import com.p10.stats.config.OPEN_F1_CLIENT
import com.p10.stats.model.SessionType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import java.util.Optional

@Component
class OpenF1SessionsClient(
    @Qualifier(OPEN_F1_CLIENT) private val webClient: WebClient,
) {
    fun getGpsBasicDetails(
        year: Int,
        circuitName: String? = null,
    ): List<GpBaseDetails>? =
        webClient
            .get()
            .uri("/sessions") { uriBuilder ->
                uriBuilder
                    .queryParam("year", year)
                    .queryParamIfPresent("circuit_short_name", Optional.ofNullable(circuitName))
                    .queryParam("session_name", SessionType.SPRINT.value, SessionType.RACE.value)
                    .build()
            }.retrieve()
            .bodyToFlux<GpBaseDetails>()
            .collectList()
            .block()

    fun getTrackScheduleDetails(
        year: Int,
        location: String,
    ): List<GpTrackScheduleDetails>? =
        webClient
            .get()
            .uri("/sessions") { uriBuilder ->
                uriBuilder
                    .queryParam("year", year)
                    .queryParam("location", location)
                    .build()
            }.retrieve()
            .bodyToFlux<GpTrackScheduleDetails>()
            .collectList()
            .block()

    fun getStartingGrid(sessionKey: Int): List<GpGridInfo>? =
        webClient
            .get()
            .uri("/starting_grid") { uriBuilder ->
                uriBuilder
                    .queryParam("session_key", sessionKey)
                    .build()
            }.retrieve()
            .bodyToFlux<GpGridInfo>()
            .collectList()
            .block()
}
