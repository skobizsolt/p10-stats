package com.p10.stats.util

import com.p10.stats.model.SessionType

object Leaderboard {
    fun findPointsForPosition(
        position: Int,
        sessionType: SessionType,
    ) = when (sessionType) {
        SessionType.SPRINT -> sprintPoints[position] ?: 0
        SessionType.RACE -> racePoints[position] ?: 0
        else -> throw Exception("Invalid session type: $sessionType")
    }

    private val racePoints =
        mapOf(
            1 to 1,
            2 to 2,
            3 to 4,
            4 to 6,
            5 to 8,
            6 to 10,
            7 to 12,
            8 to 15,
            9 to 18,
            10 to 25,
            11 to 18,
            12 to 15,
            13 to 12,
            14 to 10,
            15 to 8,
            16 to 6,
            17 to 4,
            18 to 2,
            19 to 1,
            20 to 1,
            21 to 1,
            22 to 1,
        )

    private val sprintPoints =
        mapOf(
            3 to 1,
            4 to 2,
            5 to 3,
            6 to 4,
            7 to 5,
            8 to 6,
            9 to 7,
            10 to 8,
            11 to 7,
            12 to 6,
            13 to 5,
            14 to 4,
            15 to 3,
            16 to 2,
            17 to 1,
        )
}
