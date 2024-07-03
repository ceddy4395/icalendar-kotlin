package dev.willekens.icalendar

import dev.willekens.icalendar.ContentLine.Companion.IANA_TOKEN_REGEX

data class ContentLineToken (val value: String) {
    init {
        require(value.matches(IANA_TOKEN_REGEX)) { "Invalid token: \"$value\"" }
    }
    override fun toString() = value.uppercase()
    override fun equals(other: Any?) = (other is ContentLineToken) && value.equals(other.value, true)
    override fun hashCode(): Int = value.uppercase().hashCode()
}
