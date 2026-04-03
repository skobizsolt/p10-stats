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
            .getGpsBasicDetails(year)
            .orEmpty()
            .sortedBy { it.endDate }
            .groupBySessionType()

    fun getRaceByCircuitName(
        selectedYear: Int,
        circuitName: String,
    ): Map<SessionType, GpBaseDetails?> =
        sessionsClient
            .getGpsBasicDetails(year = selectedYear, circuitName = circuitName)
            .orEmpty()
            .groupBySessionType()
            .mapValues { it.value.firstOrNull() }
            .ifEmpty { throw Exception("Race weekend data not found for $selectedYear - $circuitName") }

    fun getRaceCircuits(year: Int): List<String> =
        getRaces(year)[SessionType.RACE]
            .orEmpty()
            .mapNotNull { it?.circuitName }

    private fun List<GpBaseDetails>.groupBySessionType() =
        groupBy {
            SessionType.forValue(it.sessionName)
                ?: throw Exception("Session name ${it.sessionName} unrecognized!")
        }
}
