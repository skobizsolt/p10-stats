package com.p10.stats.model

import com.p10.stats.client.domain.GpDriverInfo
import com.p10.stats.client.domain.GpGridInfo

data class StartingGridData(
    val startingPositions: List<GpGridInfo>,
    val driversData: List<GpDriverInfo>,
)
