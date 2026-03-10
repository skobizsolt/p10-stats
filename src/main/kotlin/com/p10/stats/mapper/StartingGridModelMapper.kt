package com.p10.stats.mapper

import com.p10.stats.client.domain.GpDriverInfo
import com.p10.stats.client.domain.GpGridInfo
import com.p10.stats.generated.GridPositionData
import com.p10.stats.model.StartingGridData
import org.springframework.stereotype.Component

@Component
class StartingGridModelMapper {
    fun toGrid(startingGridData: StartingGridData): List<GridPositionData>? =
        startingGridData.run {
            startingPositions
                .sortedBy { it.position }
                .map { gridSlot ->
                    toPosition(
                        gridSlot = gridSlot,
                        driverData =
                            driversData.firstOrNull { it.driverNumber == gridSlot.driverNumber }
                                ?: throw Exception("Driver data not found for grid position '${gridSlot.position}'"),
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
}
