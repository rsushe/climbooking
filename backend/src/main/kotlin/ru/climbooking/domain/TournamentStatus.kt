package ru.climbooking.domain

import java.lang.IllegalArgumentException

enum class TournamentStatus(private val status: String) {
    PLANNED("planned"),
    ONGOING("ongoing"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    companion object {
        fun ofStatus(status: String): TournamentStatus =
            TournamentStatus.values().firstOrNull { it.status == status }
                ?: throw IllegalArgumentException("Can't find tournament status for status $status")
    }
}
