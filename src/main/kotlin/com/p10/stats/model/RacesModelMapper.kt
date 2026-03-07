package com.p10.stats.model

import com.p10.stats.generated.CircuitRecord
import com.p10.stats.generated.GetRaceCircuitsResponse
import com.p10.stats.generated.GetRacesResponse
import com.p10.stats.generated.RaceDetails
import com.p10.stats.repository.domain.GpBaseDetails
import org.springframework.stereotype.Component

@Component
class RacesModelMapper {
    fun toRacesResponse(
        year: Int,
        sessionsByType: Map<SessionType, List<GpBaseDetails?>>,
    ): GetRacesResponse {
        sessionsByType[SessionType.RACE]
            .orEmpty()
            .sortedBy { it?.endDate }
            .let { races ->
                return GetRacesResponse(
                    year = year,
                    races =
                        races.mapIndexed { roundId, race ->
                            toRaceDetails(
                                roundId = roundId + 1,
                                race = checkNotNull(race) { "Race details should not be null" },
                                sprint = sessionsByType[SessionType.SPRINT]?.firstOrNull { sprint -> sprint?.circuitName == race.circuitName },
                            )
                        },
                )
            }
    }

    fun toRaceCircuits(
        year: Int,
        circuits: List<String>,
    ): GetRaceCircuitsResponse =
        GetRaceCircuitsResponse(
            year = year,
            circuits =
                circuits.map {
                    CircuitRecord(name = it)
                },
        )

    private fun toRaceDetails(
        roundId: Int,
        race: GpBaseDetails,
        sprint: GpBaseDetails?,
    ): RaceDetails =
        RaceDetails(
            roundId = roundId,
            circuitName = race.circuitName,
            isSpringWeekend = sprint != null,
            sprintDate = sprint?.endDate?.toLocalDate(),
            raceDate = race.endDate.toLocalDate(),
            raceSessionKey = race.sessionKey,
            sprintSessionKey = sprint?.sessionKey,
        )
}
