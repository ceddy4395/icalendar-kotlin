package dev.willekens.icalendar

import java.time.ZoneId

class CalendarPropertyBuilder(var name: ContentLineToken, var context: CalendarContext,
                              val localTimeZone: ZoneId = ZoneId.systemDefault()
) {
    private var params: Map<ContentLineToken, String> = emptyMap()
    private var value: String? = null

    fun params(params: Map<ContentLineToken, String>) : CalendarPropertyBuilder {
        this.params = params
        return this
    }

    fun value(value: String) : CalendarPropertyBuilder {
        this.value = value
        return this
    }

    fun build(): CalendarProperty {
        check(value != null) { "value must be provided" }
        return when (name) {
            DTSTART, DTEND -> FlexDateTimeProperty(
                name,
                value.orEmpty(),
                params,
                context,
                localTimeZone
            )
            DURATION -> DurationProperty(value.orEmpty(), params)
            RRULE -> RecurrenceRuleProperty(value.orEmpty(), params)
            else -> CalendarProperty(name, value.orEmpty(), params, context, localTimeZone)
        }
    }
}
