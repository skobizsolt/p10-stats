package com.p10.stats.api

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
        require(year >= 2023) { "year should be greater than 2023" }

        val racesBySessionType = racesService.getRaces(year)
        val response = racesModelMapper.toRacesResponse(racesBySessionType)

        return ResponseEntity.ok().body(response)
    }
}
