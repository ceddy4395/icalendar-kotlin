package dev.willekens.icalendar

open class CalendarComponent(val name: ContentLineToken, val components: List<CalendarComponent> = emptyList(), val properties: List<CalendarProperty> = emptyList()) {
    fun findPropertyByName(name: ContentLineToken) : CalendarProperty? {
        val filtered = properties.filter { it.name == name }
        require(filtered.size < 2) { "Expected $name to have only 1 value. Actual: ${filtered.size}" }
        return filtered.firstOrNull()
    }

    fun isPresent(name: ContentLineToken): Boolean {
        return properties.any { it.name == name }
    }

    fun requireProperties(vararg propertyNames: ContentLineToken) {
        propertyNames.forEach { name -> require(isPresent(name)) { "A $name property is required" } }
    }

    fun getPropertyValue(name: ContentLineToken) : String? {
        return findPropertyByName(name)?.value
    }

    override fun toString(): String {
        return (properties + components).joinToString(OUTPUT_LINE_END, "BEGIN:$name$OUTPUT_LINE_END", "${OUTPUT_LINE_END}END:$name")
    }

    inline fun <reified R : CalendarProperty> getTypedProperty(name: ContentLineToken, map: (CalendarProperty) -> R): R? {
        val property = findPropertyByName(name)
        return when (property) {
            is R -> property
            null -> null
            else -> map(property)
        }
    }
}
