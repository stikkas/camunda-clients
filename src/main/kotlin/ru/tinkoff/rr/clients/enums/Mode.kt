package ru.tinkoff.rr.clients.enums

/**
 * Modes of program process
 */
enum class Mode(val code: String, val description: String) {
    INCIDENTS("incidents", "retry incidents for a process definition"),
    MANUAL("manual", "reset user tasks");

    override fun toString(): String {
        return "$code - $description"
    }

    companion object {
        fun fromString(code: String): Mode? = values().find { it.code == code }
    }
}
