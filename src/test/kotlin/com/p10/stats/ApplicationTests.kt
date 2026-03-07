package com.p10.stats

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(value = ["local"])
class ApplicationTests {
    @Test
    fun contextLoads() {
    }
}
