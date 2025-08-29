package com.vandrushko.core.domain.settings.model

enum class UpdatePeriod(val seconds: Int, val millis: Long) {
    ONE_SECOND(seconds = 1, millis = 1000L),
    TWO_SECOND(seconds = 2, millis = 2000L),
    FIVE_SECOND(seconds = 5, millis = 5000L)
}