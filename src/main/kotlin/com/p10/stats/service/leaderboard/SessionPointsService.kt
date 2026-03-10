package com.p10.stats.service.leaderboard

import com.p10.points.generated.PostSessionPointsRequest
import com.p10.points.generated.PostSessionPointsResponse
import com.p10.points.generated.PredictionResult
import com.p10.stats.model.SessionType
import com.p10.stats.service.grid.GridService
import com.p10.stats.util.Leaderboard
import com.p10.stats.util.RequestProperties
import org.springframework.stereotype.Service

@Service
class SessionPointsService(
    private val gridService: GridService,
) {
    fun calculateSessionPoints(request: PostSessionPointsRequest): PostSessionPointsResponse {
        request.run {
            require(predictions.isEmpty().not()) { "tips should not be empty" }
            val raceResult =
                gridService.getRaceResults(
                    circuitName = circuitName,
                    year = RequestProperties.getYear(year),
                    sprintGrid = sprintRace == true,
                )

            return PostSessionPointsResponse(
                year = RequestProperties.getYear(year),
                circuitName = circuitName,
                firstDnf = raceResult.firstDnfs,
                results =
                    predictions
                        .map {
                            PredictionResult(
                                name = it.name,
                                driverPosition = raceResult.driverPosition(it.driver),
                                predictedDriver = raceResult.driver(it.driver).driverName,
                                pointsForDriver =
                                    Leaderboard.findPointsForPosition(
                                        position = raceResult.driverPosition(it.driver),
                                        sessionType = if (sprintRace == true) SessionType.SPRINT else SessionType.RACE,
                                    ),
                                predictedDnfDriver = raceResult.driver(it.dnfDriver).driverName,
                                dnfCorrect = raceResult.driverIsDnf(it.driver),
                            )
                        },
            )
        }
    }
}
