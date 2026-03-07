package com.p10.stats.api

import com.p10.stats.generated.GetRaceCircuitsResponse
import com.p10.stats.generated.GetRacesResponse
import com.p10.stats.generated.api.RacesApi
import com.p10.stats.model.RacesModelMapper
import com.p10.stats.service.races.RacesService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class RacesApiController(
    private val racesService: RacesService,
    private val racesModelMapper: RacesModelMapper,
) : RacesApi {
    override fun getRaces(year: Int?): ResponseEntity<GetRacesResponse> {
        val selectedYear = getYear(year)
        val racesBySessionType = racesService.getRaces(selectedYear)
        val response = racesModelMapper.toRacesResponse(selectedYear, racesBySessionType)

        return ResponseEntity.ok().body(response)
    }

    override fun getRaceCircuits(year: Int?): ResponseEntity<GetRaceCircuitsResponse> {
        val selectedYear = getYear(year)
        val circuits = racesService.getRaceCircuits(selectedYear)
        val response = racesModelMapper.toRaceCircuits(selectedYear, circuits)

        return ResponseEntity.ok().body(response)
    }

    private fun getYear(year: Int?): Int {
        val selectedYear = year ?: LocalDate.now().year
        require(selectedYear >= 2023) { "year should be greater than 2023" }
        return selectedYear
    }
}
