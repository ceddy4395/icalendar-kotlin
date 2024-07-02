package dev.willekens.icalendar

import java.time.ZoneId

class CalendarTimeZone (components: List<CalendarComponent> = ArrayList(),
                        properties: List<CalendarProperty>) : CalendarComponent(
    VTIMEZONE, components, properties) {
    private val locationExtension = getPropertyValue(X_LIC_LOCATION)
    val timeZoneId = getPropertyValue(TZID)

    // Check to see if X-LIC-LOCATION is present.  This is non standard so not always available.  Otherwise check the
    // TZID as this is often the a common Time Zone name.  If not, then this library does not yet support the time zone.
    val timeZone: ZoneId =
        ZoneId.of(if (!locationExtension.isNullOrEmpty()) locationExtension else timeZoneId)
}
