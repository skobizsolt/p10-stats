package com.p10.stats.client.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class GpResultInfo(
    @JsonProperty("position")
    val position: Int? = null,
    @JsonProperty("driver_number")
    val driverNumber: Int,
    @JsonProperty("dnf")
    val dnf: Boolean,
    @JsonProperty("dsq")
    val dsq: Boolean,
    @JsonProperty("dns")
    val dns: Boolean,
    @JsonProperty("number_of_laps")
    val numberOfLaps: Int,
)
