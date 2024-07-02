package dev.willekens.icalendar

import java.time.ZoneId

// TODO variables perhaps shouldn't be optional, in particular context
open class CalendarProperty(val name: ContentLineToken, private val rawValue: String, val params: Map<ContentLineToken, String> = emptyMap(),
                            val context: CalendarContext = CalendarContext(), val localTimeZone: ZoneId = ZoneId.systemDefault()
) {
    val value = rawValue.replace("\\n", "\n").replace("\\r", "\r")
            .replace("\\,", ",").replace("\\;", ";").replace("\\:", ":")
            .replace("\\\"", "\"").replace("\\\\", "\\")

    override fun toString(): String {
        val paramStr = if (params.isNotEmpty()) params.toList().joinToString(";", ";" ) { "${it.first}=${it.second}" } else ""
        val unfoldedResult = "$name$paramStr:$rawValue"
        return unfoldedResult.chunked(75).joinToString("$OUTPUT_LINE_END ")
    }
}
