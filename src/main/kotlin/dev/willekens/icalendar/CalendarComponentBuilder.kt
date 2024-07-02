package dev.willekens.icalendar

import java.util.Collections.unmodifiableList

class CalendarComponentBuilder(var name: ContentLineToken, var context: CalendarContext) {
    private val components: MutableList<CalendarComponent> = ArrayList()
    private val properties: MutableList<CalendarProperty> = ArrayList()

    fun add(component: CalendarComponent) : CalendarComponentBuilder {
        components.add(component)
        return this
    }

    fun add(property: CalendarProperty) : CalendarComponentBuilder {
        properties.add(property)
        return this
    }

    fun build(): CalendarComponent {
        check(components.isNotEmpty() || properties.isNotEmpty()) { "At least one component or property must be provided" }
        val components = unmodifiableList(components.toList())
        val properties = unmodifiableList(properties.toList())
        return when (name) {
            VCALENDAR -> Calendar(components, properties)
            VEVENT -> Event(components, properties, context)
            VTIMEZONE -> CalendarTimeZone(components, properties)
            else -> CalendarComponent(name, components, properties)
        }
    }
}

