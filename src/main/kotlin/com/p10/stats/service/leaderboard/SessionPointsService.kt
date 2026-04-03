package com.p10.stats.service.leaderboard

import com.p10.points.generated.DnfResult
import com.p10.points.generated.GpPredictionResult
import com.p10.points.generated.PostSessionPointsRequest
import com.p10.points.generated.PostSessionPointsResponse
import com.p10.points.generated.SessionResult
import com.p10.stats.model.RaceResultData
import com.p10.stats.model.SessionType
import com.p10.stats.service.grid.GridService
import com.p10.stats.service.races.RacesService
import com.p10.stats.util.Leaderboard
import com.p10.stats.util.RequestProperties
import org.springframework.stereotype.Service

@Service
class SessionPointsService(
    private val racesService: RacesService,
    private val gridService: GridService,
) {
    fun calculateSessionPoints(request: PostSessionPointsRequest): PostSessionPointsResponse {
        request.run {
            require(predictions.isEmpty().not()) { "tips should not be empty" }

            val selectedYear = RequestProperties.getYear(year)
            val raceDetails =
                racesService.getRaceByCircuitName(selectedYear, circuitName)

            val sprintResult =
                if (raceDetails.containsKey(SessionType.SPRINT)) {
                    gridService.getRaceResults(circuitName = circuitName, year = selectedYear, sprintGrid = true)
                } else {
                    null
                }

            val raceResult = gridService.getRaceResults(circuitName = circuitName, year = selectedYear)

            val gpResults =
                predictions.map { prediction ->
                    GpPredictionResult(
                        name = prediction.name,
                        sprintResult =
                            prediction.sprintDriver?.let { sprintDriver ->
                                sprintResult?.sessionResult(sprintDriver, SessionType.SPRINT)
                            },
                        raceResult =
                            prediction.raceDriver?.let { raceDriver ->
                                raceResult.sessionResult(raceDriver, SessionType.RACE)
                            },
                        dnfResult =
                            prediction.dnfDriver?.let { dnfDriver ->
                                DnfResult(
                                    driver = raceResult.driverName(dnfDriver),
                                    isFirstDnf = raceResult.driverIsDnf(dnfDriver),
                                )
                            },
                    )
                }

            return PostSessionPointsResponse(
                year = RequestProperties.getYear(year),
                circuitName = circuitName,
                firstDnf = raceResult.firstDnfs,
                gpResults = gpResults,
            )
        }
    }

    fun RaceResultData.sessionResult(
        driverName: String,
        sessionType: SessionType,
    ) = SessionResult(
        driver = this.driverName(driverName),
        position = this.driverPosition(driverName),
        points =
            Leaderboard.findPointsForPosition(
                position = this.driverPosition(driverName),
                sessionType = sessionType,
            ),
    )
}
