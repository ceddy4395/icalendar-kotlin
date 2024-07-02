package dev.willekens.icalendar

import java.time.temporal.ChronoField
import java.time.temporal.TemporalField

enum class RecurrenceFrequency(temporalField: TemporalField) {
    SECONDLY(ChronoField.SECOND_OF_MINUTE),
    MINUTELY(ChronoField.MINUTE_OF_HOUR),
    HOURLY(ChronoField.HOUR_OF_DAY),
    DAILY(ChronoField.DAY_OF_MONTH),
    WEEKLY(ChronoField.ALIGNED_WEEK_OF_YEAR),
    MONTHLY(ChronoField.MONTH_OF_YEAR),
    YEARLY(ChronoField.YEAR);
    val temporalUnit = temporalField.baseUnit
}
