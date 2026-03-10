package com.p10.stats.client.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class GpTrackScheduleDetails(
    @JsonProperty("session_key")
    val sessionKey: Int,
    @JsonProperty("session_name")
    val sessionName: String,
    @JsonProperty("date_start")
    val startDate: OffsetDateTime,
    @JsonProperty("date_end")
    val endDate: OffsetDateTime,
)
