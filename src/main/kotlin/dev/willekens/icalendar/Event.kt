package dev.willekens.icalendar

import dev.willekens.icalendar.RecurrenceRuleProperty.Companion.UNTIL_KEY
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.Temporal

class Event(components: List<CalendarComponent> = ArrayList(), properties:
List<CalendarProperty>, context: CalendarContext = CalendarContext()) : CalendarComponent(
    VEVENT, components, properties) {
    private val startTimeProperty = getTypedProperty(DTSTART, FlexDateTimeProperty.from)
    private val endTimeProperty = getTypedProperty(DTEND, FlexDateTimeProperty.from)
    private val durationProperty = getTypedProperty(DURATION, DurationProperty.from)
    private val recurrenceRuleProperty = getTypedProperty(RRULE, RecurrenceRuleProperty.from)

    val uid = getPropertyValue(UID);
    val summary = getPropertyValue(SUMMARY).orEmpty()
    val description = getPropertyValue(DESCRIPTION).orEmpty()
    val location = getPropertyValue(LOCATION).orEmpty()

    val dateType = startTimeProperty?.type
    val start = startTimeProperty?.temporalValue
    val end: Temporal?
    val recurrences: List<Temporal>? = if (start != null && recurrenceRuleProperty != null) calculateRecurrences(
        start,
        recurrenceRuleProperty
    ) else null

    init {
        requireProperties(UID)
        require(startTimeProperty != null || context.method != null) { "A $DTSTART property is required when the calendar doesn't have a $METHOD property" }
        require(endTimeProperty == null || durationProperty == null) { "Only one of the $DTEND or $DURATION properties is allowed" }

        if (recurrenceRuleProperty != null) {
            if (dateType == FlexDateTimeType.DATE) {
                require(recurrenceRuleProperty.bySecondList == null && recurrenceRuleProperty.byMinuteList == null && recurrenceRuleProperty.byHourList == null) {
                    "A recurrence may not specify a time component when $DTSTART is ${FlexDateTimeType.DATE} type"
                }
                require(recurrenceRuleProperty.until == null || recurrenceRuleProperty.until is LocalDate) { "Expected $RRULE to have an $UNTIL_KEY property of type ${FlexDateTimeType.DATE} to match $DTSTART" }
            }
            else {
                require(recurrenceRuleProperty.until == null || recurrenceRuleProperty.until is ZonedDateTime) { "Expected $RRULE to have an $UNTIL_KEY property of type ${FlexDateTimeType.DATETIME} to match $DTSTART" }
            }
        }

        end = when {
            endTimeProperty != null -> {
                require(endTimeProperty.type == dateType) { "$DTEND type does not match $DTSTART. Expected $dateType but was ${endTimeProperty.type}" }
                require(startTimeProperty != null) { "$DTEND was provided but $DTSTART is null" }
                require(endTimeProperty > startTimeProperty) { "$DTEND (${endTimeProperty.temporalValue}) must be later than $DTSTART (${startTimeProperty.temporalValue})" }
                endTimeProperty.temporalValue
            }
            durationProperty != null -> {
                require(dateType == FlexDateTimeType.DATETIME || durationProperty.dateType == FlexDateTimeType.DATE) { "When $DTSTART is a ${FlexDateTimeType.DATE} then $DURATION cannot have a time component: ${durationProperty.value}" }
                start?.plus(durationProperty.duration)
            }
            else -> null
        }
    }

    fun calculateRecurrences(originalStart: Temporal, recurrenceRule: RecurrenceRuleProperty): List<Temporal> {
        return RecurrenceGenerator(recurrenceRule, originalStart).calculateRecurrences()
    }
}
