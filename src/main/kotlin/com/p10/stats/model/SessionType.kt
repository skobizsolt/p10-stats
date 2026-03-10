package com.p10.stats.model

enum class SessionType(
    val value: String,
) {
    PRACTICE_1("Practice 1"),
    PRACTICE_2("Practice 2"),
    PRACTICE_3("Practice 3"),
    SPRINT("Sprint"),
    SPRINT_QUALIFYING("Sprint Qualifying"),
    QUALIFYING("Qualifying"),
    RACE("Race"),
    ;

    companion object {
        fun forValue(value: String) = SessionType.entries.firstOrNull { it.value == value }
    }
}
