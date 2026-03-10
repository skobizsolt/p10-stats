package com.p10.stats.client.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class GpGridInfo(
    @JsonProperty("position")
    val position: Int,
    @JsonProperty("driver_number")
    val driverNumber: Int,
)
