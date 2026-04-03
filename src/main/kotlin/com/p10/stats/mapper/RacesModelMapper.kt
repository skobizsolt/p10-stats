package com.p10.stats.mapper

import com.p10.stats.client.domain.GpBaseDetails
import com.p10.stats.generated.CircuitRecord
import com.p10.stats.generated.GetRaceCircuitsResponse
import com.p10.stats.generated.GetRacesResponse
import com.p10.stats.generated.RaceDetails
import com.p10.stats.model.SessionType
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
                        races.map { race ->
                            toRaceDetails(
                                race = checkNotNull(race) { "Race details should not be null" },
                                sprint =
                                    sessionsByType[SessionType.SPRINT]?.firstOrNull { sprint ->
                                        sprint?.circuitName == race.circuitName
                                    },
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

    fun toRaceDetails(
        race: GpBaseDetails,
        sprint: GpBaseDetails?,
    ): RaceDetails =
        RaceDetails(
            circuitName = race.circuitName,
            isSpringWeekend = sprint != null,
            sprintDate = sprint?.endDate?.toLocalDate(),
            raceDate = race.endDate.toLocalDate(),
            raceSessionKey = race.sessionKey,
            sprintSessionKey = sprint?.sessionKey,
        )
}
