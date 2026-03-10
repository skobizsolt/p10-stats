package com.p10.stats.util

import java.time.LocalDate

object RequestProperties {
    fun getYear(year: Int?): Int {
        val selectedYear = year ?: LocalDate.now().year
        require(selectedYear >= 2023) { "year should be greater than 2023" }
        return selectedYear
    }
}
