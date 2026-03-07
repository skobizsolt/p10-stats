package com.p10.stats.api

import com.p10.stats.generated.GetRaceCircuitsResponse
import com.p10.stats.generated.GetRacesResponse
import com.p10.stats.generated.api.RacesApi
import com.p10.stats.model.RacesModelMapper
import com.p10.stats.service.races.RacesService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class RacesApiController(
    private val racesService: RacesService,
    private val racesModelMapper: RacesModelMapper,
) : RacesApi {
    override fun getRaces(year: Int): ResponseEntity<GetRacesResponse> {
        validateYear(year)

        val racesBySessionType = racesService.getRaces(year)
        val response = racesModelMapper.toRacesResponse(racesBySessionType)

        return ResponseEntity.ok().body(response)
    }

    override fun getRaceCircuits(year: Int): ResponseEntity<GetRaceCircuitsResponse> {
        validateYear(year)

        val circuits = racesService.getRaceCircuits(year)
        val response = racesModelMapper.toRaceCircuits(circuits)

        return ResponseEntity.ok().body(response)
    }

    private fun validateYear(year: Int) {
        require(year >= 2023) { "year should be greater than 2023" }
    }
}
