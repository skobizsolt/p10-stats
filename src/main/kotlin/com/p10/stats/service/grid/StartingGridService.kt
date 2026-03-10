package com.p10.stats.service.grid

import com.p10.stats.client.OpenF1DriversClient
import com.p10.stats.client.OpenF1SessionsClient
import com.p10.stats.model.SessionType
import com.p10.stats.model.StartingGridData
import com.p10.stats.service.races.RacesService
import org.springframework.stereotype.Service

@Service
class StartingGridService(
    private val racesService: RacesService,
    private val sessionsClient: OpenF1SessionsClient,
    private val driversClient: OpenF1DriversClient,
) {
    fun getStartingGrid(
        circuitName: String,
        year: Int,
        sprintGrid: Boolean,
    ): StartingGridData {
        val qualifyingType = if (sprintGrid) SessionType.SPRINT_QUALIFYING else SessionType.QUALIFYING

        val qualifyingSessionKey =
            sessionsClient
                .getTrackScheduleDetails(year, circuitName)
                ?.associateBy { SessionType.forValue(it.sessionName) }[qualifyingType]
                ?.sessionKey
                ?: throw Exception("$qualifyingType data not found for circuit '$circuitName'")

        val gridData = sessionsClient.getStartingGrid(qualifyingSessionKey)
        val driverData = driversClient.getDriversData(qualifyingSessionKey)

        return StartingGridData(
            startingPositions = gridData.orEmpty(),
            driversData = driverData.orEmpty(),
        )
    }
}
