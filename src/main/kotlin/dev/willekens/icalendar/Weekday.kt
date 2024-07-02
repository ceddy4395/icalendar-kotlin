package dev.willekens.icalendar

import java.time.DayOfWeek

enum class Weekday {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    val symbol = this.name.substring(0, 2)
    val dayOfWeek = DayOfWeek.of(ordinal + 1)

    override fun toString(): String {
        return symbol
    }

    companion object {
        fun findBySymbol(symbol: String): Weekday {
            val result = Weekday.values().find { it.symbol == symbol }
            require(result != null) { "Weekday not recognized: $symbol" }
            return result
        }
    }
}
