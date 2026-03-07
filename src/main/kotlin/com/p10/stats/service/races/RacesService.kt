package com.p10.stats.service.races

import com.p10.stats.client.OpenF1SessionsClient
import com.p10.stats.model.SessionType
import com.p10.stats.repository.domain.GpBaseDetails
import org.springframework.stereotype.Service

@Service
class RacesService(
    private val sessionsClient: OpenF1SessionsClient,
) {
    fun getRaces(year: Int): Map<SessionType, List<GpBaseDetails?>> =
        sessionsClient
            .getGpsBasicDatailsForYear(year)
            .sortedBy { it.endDate }
            .groupBy { it.circuitName }
            .let { sessionsByCircuit ->
                sessionsByCircuit.keys
                    .map { name ->
                        val raceSessions = checkNotNull(sessionsByCircuit[name]) { "Race details should not be null" }
                        listOfNotNull(
                            if (raceSessions.size > 1) SessionType.SPRINT to raceSessions.firstOrNull() else null,
                            SessionType.RACE to raceSessions.last(),
                        )
                    }.flatten()
                    .groupBy({ it.first }, { it.second })
            }
}
