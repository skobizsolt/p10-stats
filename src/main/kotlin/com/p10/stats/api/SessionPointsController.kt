package com.p10.stats.api

import com.p10.points.generated.PostSessionPointsRequest
import com.p10.points.generated.PostSessionPointsResponse
import com.p10.points.generated.api.SessionPointsApi
import com.p10.stats.service.leaderboard.SessionPointsService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class SessionPointsController(
    private val sessionPointsService: SessionPointsService,
) : SessionPointsApi {
    override fun calculateSessionPoints(postSessionPointsRequest: PostSessionPointsRequest): ResponseEntity<PostSessionPointsResponse> =
        ResponseEntity
            .ok()
            .body(sessionPointsService.calculateSessionPoints(postSessionPointsRequest))
}
