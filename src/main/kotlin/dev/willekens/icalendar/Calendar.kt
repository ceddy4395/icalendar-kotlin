package dev.willekens.icalendar

import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal const val OUTPUT_LINE_END = "\r\n"
internal const val DATE_FORMAT = "yyyyMMdd";
internal const val TIME_FORMAT = "HHmmss";
internal val DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
internal val DATETIME_FORMATTER = DateTimeFormatter.ofPattern("$DATE_FORMAT'T'$TIME_FORMAT[X]")
internal val UTC_TZ = ZoneId.of("UTC")

internal val BEGIN = ContentLineToken("BEGIN")
internal val END = ContentLineToken("END")
internal val VCALENDAR = ContentLineToken("VCALENDAR")
internal val VEVENT = ContentLineToken("VEVENT")
internal val VTIMEZONE = ContentLineToken("VTIMEZONE")

internal val PRODID = ContentLineToken("PRODID")
internal val VERSION = ContentLineToken("VERSION")
internal val CALSCALE = ContentLineToken("CALSCALE")
internal val METHOD = ContentLineToken("METHOD")
internal val UID = ContentLineToken("UID")
internal val DTSTAMP = ContentLineToken("DTSTAMP")
internal val DTSTART = ContentLineToken("DTSTART")
internal val DTEND = ContentLineToken("DTEND")
internal val DURATION = ContentLineToken("DURATION")
internal val RRULE = ContentLineToken("RRULE")
internal val SUMMARY = ContentLineToken("SUMMARY")
internal val DESCRIPTION = ContentLineToken("DESCRIPTION")
internal val LOCATION = ContentLineToken("LOCATION")

internal val TZID = ContentLineToken("TZID")
internal val X_LIC_LOCATION = ContentLineToken("X-LIC-LOCATION")

internal const val GREGORIAN_SCALE = "GREGORIAN"
internal val SUPPORTED_VERSIONS = listOf("2.0")


class Calendar(components: List<CalendarComponent> = ArrayList(),
               properties: List<CalendarProperty>) : CalendarComponent(
    VCALENDAR, components, properties) {

    val events = components.filterIsInstance<Event>()
    val prodId = getPropertyValue(PRODID)
    val version = getPropertyValue(VERSION)
    val calendarScale = getPropertyValue(CALSCALE) ?: GREGORIAN_SCALE
    val method = getPropertyValue(METHOD)

    init {
        require(prodId != null) { "A $PRODID property is required" }
        require(SUPPORTED_VERSIONS.contains(version)) { "The $VERSION property is unsupported. It must be one of: ${SUPPORTED_VERSIONS.joinToString(", ")}" }
        require(calendarScale == GREGORIAN_SCALE) { "Invalid $CALSCALE property provided, only \"$GREGORIAN_SCALE\" is supported" }
        require(components.isNotEmpty()) { "Calendar must have at least 1 component" }
    }
}

