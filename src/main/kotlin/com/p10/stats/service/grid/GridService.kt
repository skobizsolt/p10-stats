package com.p10.stats.service.grid

import com.p10.stats.client.OpenF1DriversClient
import com.p10.stats.client.OpenF1SessionResultClient
import com.p10.stats.client.OpenF1SessionsClient
import com.p10.stats.model.RaceResultData
import com.p10.stats.model.SessionType
import com.p10.stats.model.StartingGridData
import org.springframework.stereotype.Service

@Service
class GridService(
    private val sessionsClient: OpenF1SessionsClient,
    private val driversClient: OpenF1DriversClient,
    private val sessionResultClient: OpenF1SessionResultClient,
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

    fun getRaceResults(
        circuitName: String,
        year: Int,
        sprintGrid: Boolean,
    ): RaceResultData {
        val raceType = if (sprintGrid) SessionType.SPRINT else SessionType.RACE

        val sessionKey =
            sessionsClient
                .getTrackScheduleDetails(year, circuitName)
                ?.associateBy { SessionType.forValue(it.sessionName) }[raceType]
                ?.sessionKey
                ?: throw Exception("$raceType data not found for circuit '$circuitName'")

        val raceResults =
            sessionResultClient
                .getClassification(sessionKey)
                .mapIndexed { index, info ->
                    info.let { it.copy(position = it.position ?: index) }
                }.sortedBy { it.position }

        val driverData = driversClient.getDriversData(sessionKey)

        val firstDnf =
            raceResults
                .sortedBy { it.numberOfLaps }
                .filter { it.dnf }
                .let {
                    val leastCompletedLap = it.first().numberOfLaps
                    it
                        .filter { it.numberOfLaps == leastCompletedLap }
                        .mapNotNull {
                            driverData
                                ?.firstOrNull { driver -> driver.driverNumber == it.driverNumber }
                                ?.driverName
                        }
                }

        return RaceResultData(
            finishingPositions = raceResults,
            driversData = driverData.orEmpty(),
            firstDnfs = firstDnf,
        )
    }
}
