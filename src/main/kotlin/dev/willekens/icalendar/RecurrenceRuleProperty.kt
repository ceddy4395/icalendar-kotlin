package dev.willekens.icalendar

import java.time.ZoneId
import kotlin.math.absoluteValue



class RecurrenceRuleProperty(value: String,
                                      params: Map<ContentLineToken, String> = emptyMap(),
                                      localTimeZone: ZoneId = ZoneId.systemDefault()) : CalendarProperty(
    RRULE, value, params) {
    private val parts = value.splitCalendarValueToMap()

    val frequency: RecurrenceFrequency = RecurrenceFrequency.valueOf(parts[FREQUENCY_KEY]
        ?: throw IllegalArgumentException("$FREQUENCY_KEY is required"))
    val interval: Int = parts[INTERVAL_KEY]?.toInt() ?: 1
    val count: Int? = parts[COUNT_KEY]?.toIntOrNull()
    val until = parts[UNTIL_KEY]?.parseCalendarTemporal(localTimeZone)
    val weekStart: Weekday = Weekday.findBySymbol(parts[WEEKSTART_KEY] ?: Weekday.MONDAY.symbol)

    val bySecondList = partAsIntList(BYSECOND_KEY, 0..60)
    val byMinuteList = partAsIntList(BYMINUTE_KEY, 0..59)
    val byHourList = partAsIntList(BYHOUR_KEY, 0..23)
    val byMonthDayList = partAsIntList(BYMONTHDAY_KEY, 1..31, true)
    val byYearDayList = partAsIntList(BYYEARDAY_KEY, 1..366, true)
    val byWeekNumberList = partAsIntList(BYWEEKNUMBER_KEY, 1..53, true)
    val byMonthList = partAsIntList(BYMONTH_KEY, 1..12)
    val bySetPositionList = partAsIntList(BYSETPOSITION_KEY, 1..366, true)
    val byWeekDayList = parts[BYWEEKDAY_KEY]?.split(',')?.map {
        val weekday = Weekday.findBySymbol(it.takeLast(2))
        val number = if (it.length > 2) it.dropLast(2).toInt() else 0
        require(number in -53..53)
        Pair(number, weekday)
    }

    companion object {
        private const val FREQUENCY_KEY = "FREQ"
        private const val COUNT_KEY = "COUNT"
        const val UNTIL_KEY = "UNTIL"
        private const val INTERVAL_KEY = "INTERVAL"
        private const val WEEKSTART_KEY = "WKST"

        private const val BYSECOND_KEY = "BYSECOND"
        private const val BYMINUTE_KEY = "BYMINUTE"
        private const val BYHOUR_KEY = "BYHOUR"
        private const val BYWEEKDAY_KEY = "BYDAY"
        private const val BYMONTHDAY_KEY = "BYMONTHDAY"
        private const val BYYEARDAY_KEY = "BYYEARDAY"
        private const val BYMONTH_KEY = "BYMONTH"
        private const val BYWEEKNUMBER_KEY = "BYWEEKNO"
        private const val BYSETPOSITION_KEY = "BYSETPOS"

        val from: (CalendarProperty) -> RecurrenceRuleProperty = { source ->
            require(source.name == RRULE) { "Expected property name was $RRULE, received ${source.name}" }
            RecurrenceRuleProperty(source.value, source.params)
        }
    }

    init {
        require(count == null || count > 0) { "$COUNT_KEY must be > 0" }
        require(interval > 0) { "$INTERVAL_KEY must be > 0" }
        require(count == null || until == null) { "Only one of $COUNT_KEY or $UNTIL_KEY may be provided, not both" }
        require(byWeekNumberList == null || frequency == RecurrenceFrequency.YEARLY) {
            "$BYWEEKNUMBER_KEY may only be specified when the $FREQUENCY_KEY is ${RecurrenceFrequency.WEEKLY}" }
        require(byYearDayList == null || !(frequency in listOf(
            RecurrenceFrequency.DAILY,
            RecurrenceFrequency.WEEKLY,
            RecurrenceFrequency.MONTHLY
        ))) { "$BYYEARDAY_KEY cannot be specified with a $FREQUENCY_KEY of $frequency" }
        require(byMonthDayList == null || frequency != RecurrenceFrequency.WEEKLY) {
            "$BYMONTHDAY_KEY cannot be specified with a $FREQUENCY_KEY of ${RecurrenceFrequency.WEEKLY}" }
    }

    private fun partAsIntList(key: String, validRange: IntRange, allowNegative: Boolean = false): List<Int>? {
        val result = parts[key]?.split(',')?.map { it.toInt() }
        result?.map { if (allowNegative) it.absoluteValue else it }?.forEach {
            require(it in validRange) {
                "Invalid $key value, must be in range of ${validRange.start} to ${validRange.endInclusive}: $it" } }
        return result
    }

//    companion object Copy {
//
//    }
}
