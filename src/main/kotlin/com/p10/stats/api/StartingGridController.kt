package com.p10.stats.api

import com.p10.stats.generated.GetStartingGridResponse
import com.p10.stats.generated.api.StartingGridApi
import com.p10.stats.mapper.StartingGridModelMapper
import com.p10.stats.service.grid.StartingGridService
import com.p10.stats.util.RequestProperties
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class StartingGridController(
    private val startingGridService: StartingGridService,
    private val gridModelMapper: StartingGridModelMapper,
) : StartingGridApi {
    override fun getStartingGrid(
        circuitName: String,
        year: Int?,
        sprint: Boolean,
    ): ResponseEntity<GetStartingGridResponse> {
        val selectedYear = RequestProperties.getYear(year)
        val startingGridData = startingGridService.getStartingGrid(circuitName, selectedYear, sprint)

        return ResponseEntity.ok().body(
            GetStartingGridResponse(
                year = selectedYear,
                grid = gridModelMapper.toGrid(startingGridData),
            ),
        )
    }
}
