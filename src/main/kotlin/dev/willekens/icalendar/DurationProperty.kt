package dev.willekens.icalendar

import java.time.Duration
import java.time.Period
import java.time.temporal.TemporalAmount

internal class DurationProperty(value: String, params: Map<ContentLineToken, String> = emptyMap())
    : CalendarProperty(DURATION, value, params) {
    val duration : TemporalAmount
    val dateType : FlexDateTimeType


    init {
        val PERIOD_REGEX = "^P\\d+(?:W|D)\$".toRegex()
        val isPeriod = PERIOD_REGEX.matches(value)
        duration = if (isPeriod) Period.parse(value) else Duration.parse(value)
        dateType = if (isPeriod) FlexDateTimeType.DATE else FlexDateTimeType.DATETIME

        val lazyNonPositiveMessage = { "$DURATION must be a positive non-zero value: $value" }
        when (duration) {
            is Duration -> require(duration > Duration.ZERO, lazyNonPositiveMessage)
            is Period -> require(!(duration.isNegative || duration.isZero), lazyNonPositiveMessage)
        }
    }

    companion object Copy {
        val from: (CalendarProperty) -> DurationProperty = { source ->
            require(source.name == DURATION) { "Expected property name was $DURATION, received ${source.name}" }
            DurationProperty(source.value, source.params)
        }
    }
}
