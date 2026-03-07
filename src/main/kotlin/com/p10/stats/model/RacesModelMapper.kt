package com.p10.stats.model

import com.p10.stats.generated.GetRacesResponse
import com.p10.stats.generated.RaceDetails
import com.p10.stats.repository.domain.GpBaseDetails
import org.springframework.stereotype.Component

@Component
class RacesModelMapper {
    fun toRacesResponse(response: Map<SessionType, List<GpBaseDetails?>>): GetRacesResponse {
        response[SessionType.RACE]
            .orEmpty()
            .sortedBy { it?.endDate }
            .let { races ->
                return GetRacesResponse(
                    races =
                        races.mapIndexed { roundId, race ->
                            toRaceDetails(
                                roundId = roundId + 1,
                                race = checkNotNull(race) { "Race details should not be null" },
                                sprint = response[SessionType.SPRINT]?.firstOrNull { sprint -> sprint?.circuitName == race.circuitName },
                            )
                        },
                )
            }
    }

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
