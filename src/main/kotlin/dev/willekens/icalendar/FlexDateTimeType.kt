package dev.willekens.icalendar

enum class FlexDateTimeType (val text: String) {
    DATE("DATE"), DATETIME("DATE-TIME");

    override fun toString(): String {
        return text
    }

    companion object {
        fun findByText(text: String) : FlexDateTimeType {
            return values().find { it.text == text } ?: DATETIME
        }
    }
}
