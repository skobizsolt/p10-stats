package com.p10.stats.client.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class GpBaseDetails(
    @JsonProperty("circuit_short_name")
    val circuitName: String,
    @JsonProperty("session_key")
    val sessionKey: Int,
    @JsonProperty("date_end")
    val endDate: OffsetDateTime,
)
