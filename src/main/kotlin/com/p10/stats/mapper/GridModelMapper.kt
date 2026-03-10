package com.p10.stats.mapper

import com.p10.stats.client.domain.GpDriverInfo
import com.p10.stats.client.domain.GpGridInfo
import com.p10.stats.client.domain.GpResultInfo
import com.p10.stats.generated.Classification
import com.p10.stats.generated.FinishingPositionData
import com.p10.stats.generated.GridPositionData
import com.p10.stats.model.RaceResultData
import com.p10.stats.model.StartingGridData
import org.springframework.stereotype.Component

@Component
class GridModelMapper {
    fun toGrid(startingGridData: StartingGridData): List<GridPositionData>? =
        startingGridData.run {
            startingPositions
                .sortedBy { it.position }
                .map { gridSlot ->
                    toPosition(
                        gridSlot = gridSlot,
                        driverData = driversData.getDriver(gridSlot.driverNumber),
                    )
                }
        }

    private fun toPosition(
        gridSlot: GpGridInfo,
        driverData: GpDriverInfo,
    ): GridPositionData =
        GridPositionData(
            position = gridSlot.position,
            driverName = driverData.driverName,
            driverAcronym = driverData.driverAcronym,
            driverTeam = driverData.driverTeam,
            driverNumber = gridSlot.driverNumber,
        )

    fun toFinishingPosition(raceResultData: RaceResultData): List<FinishingPositionData>? =
        raceResultData.run {
            finishingPositions
                .map {
                    toPosition(
                        gridSlot = it,
                        driverData = driversData.getDriver(it.driverNumber),
                    )
                }
        }

    private fun toPosition(
        gridSlot: GpResultInfo,
        driverData: GpDriverInfo,
    ): FinishingPositionData =
        FinishingPositionData(
            position = gridSlot.position,
            driverName = driverData.driverName,
            classification =
                when {
                    gridSlot.dns -> Classification.DNS
                    gridSlot.dnf -> Classification.DNF
                    gridSlot.dsq -> Classification.DSQ
                    else -> Classification.FINISHED
                },
        )

    private fun List<GpDriverInfo>.getDriver(driverNumber: Int): GpDriverInfo =
        firstOrNull { it.driverNumber == driverNumber }
            ?: throw Exception("Driver data not found for driver '$driverNumber'")
}
