package com.p10.stats.model

import com.p10.stats.client.domain.GpDriverInfo
import com.p10.stats.client.domain.GpResultInfo

data class RaceResultData(
    val finishingPositions: List<GpResultInfo>,
    val driversData: List<GpDriverInfo>,
    val firstDnfs: List<String>,
) {
    fun driver(driver: String): GpDriverInfo =
        driversData
            .firstOrNull { it.driverName.uppercase().contains(driver.uppercase()) }
            ?: throw Exception("Driver $driver not found")

    fun driverPosition(driver: String): Int =
        driver(driver)
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
