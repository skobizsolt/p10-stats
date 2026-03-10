package com.p10.stats.api

import com.p10.stats.generated.GetRaceResultResponse
import com.p10.stats.generated.GetStartingGridResponse
import com.p10.stats.generated.api.GridApi
import com.p10.stats.mapper.GridModelMapper
import com.p10.stats.service.grid.GridService
import com.p10.stats.util.RequestProperties
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class GridController(
    private val gridService: GridService,
    private val gridModelMapper: GridModelMapper,
) : GridApi {
    override fun getStartingGrid(
        circuitName: String,
        year: Int?,
        sprint: Boolean,
    ): ResponseEntity<GetStartingGridResponse> {
        val selectedYear = RequestProperties.getYear(year)
        val startingGridData = gridService.getStartingGrid(circuitName, selectedYear, sprint)

        return ResponseEntity.ok().body(
            GetStartingGridResponse(
                year = selectedYear,
                grid = gridModelMapper.toGrid(startingGridData),
            ),
        )
    }

    override fun getRaceResults(
        circuitName: String,
        year: Int?,
        sprint: Boolean,
    ): ResponseEntity<GetRaceResultResponse> {
        val selectedYear = RequestProperties.getYear(year)
        val raceResultData = gridService.getRaceResults(circuitName, selectedYear, sprint)

        return ResponseEntity.ok().body(
            GetRaceResultResponse(
                year = selectedYear,
                circuitName = circuitName,
                firstDnf = raceResultData.firstDnfs,
                results = gridModelMapper.toFinishingPosition(raceResultData),
            ),
        )
    }
}
