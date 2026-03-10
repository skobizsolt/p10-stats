package com.p10.stats.client.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class GpDriverInfo(
    @JsonProperty("full_name")
    val driverName: String,
    @JsonProperty("name_acronym")
    val driverAcronym: String,
    @JsonProperty("team_name")
    val driverTeam: String,
    @JsonProperty("driver_number")
    val driverNumber: Int,
)
