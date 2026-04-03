package com.p10.stats.model

import com.p10.stats.client.domain.GpDriverInfo
import com.p10.stats.client.domain.GpResultInfo

data class RaceResultData(
    val finishingPositions: List<GpResultInfo>,
    val driversData: List<GpDriverInfo>,
    val firstDnfs: List<String>,
) {
    private fun driver(driverName: String): GpDriverInfo =
        driversData
            .firstOrNull { it.driverName.uppercase().contains(driverName.uppercase()) }
            ?: throw Exception("Driver $driverName not found")

    fun driverName(driver: String): String =
        driver(driver)
            .driverName

    fun driverPosition(driverName: String): Int =
        driver(driverName)
            .let { predictedDriver ->
                finishingPositions
                    .firstOrNull { it.driverNumber == predictedDriver.driverNumber }
                    .let { driverClassification ->
                        checkNotNull(driverClassification) {
                            "${predictedDriver.driverName}'s classification data not found"
                        }
                        checkNotNull(driverClassification.position) {
                            "${predictedDriver.driverName} position should not be null"
                        }

                        driverClassification.position
                    }
            }

    fun driverIsDnf(driver: String): Boolean = driver(driver)?.driverName in firstDnfs
}
