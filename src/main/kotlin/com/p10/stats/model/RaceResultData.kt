package com.p10.stats.model

import com.p10.stats.client.domain.GpDriverInfo
import com.p10.stats.client.domain.GpGridInfo
import com.p10.stats.client.domain.GpResultInfo

data class RaceResultData(
    val finishingPositions: List<GpResultInfo>,
    val driversData: List<GpDriverInfo>,
    val firstDnfs: List<String>
)
