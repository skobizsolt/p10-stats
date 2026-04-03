package com.p10.stats.util

inline fun <reified T> T.delayNextRequest(): T =
    apply {
        Thread.sleep(200)
    }
