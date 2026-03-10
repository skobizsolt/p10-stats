package com.p10.stats.service.races

import com.p10.stats.client.OpenF1SessionsClient
import com.p10.stats.client.domain.GpBaseDetails
import com.p10.stats.model.SessionType
import org.springframework.stereotype.Service

@Service
class RacesService(
    private val sessionsClient: OpenF1SessionsClient,
) {
    fun getRaces(year: Int): Map<SessionType, List<GpBaseDetails?>> =
        sessionsClient
            .getGpsBasicDatailsForYear(year)
            .orEmpty()
            .sortedBy { it.endDate }
            .groupBy {
                SessionType.forValue(it.sessionName)
                    ?: throw Exception("Session name ${it.sessionName} unrecognized!")
            }

    fun getRaceCircuits(year: Int): List<String> =
        getRaces(year)[SessionType.RACE]
            .orEmpty()
            .mapNotNull { it?.circuitName }
}
