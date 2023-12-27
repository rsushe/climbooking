package ru.climbooking.domain

import java.lang.IllegalArgumentException

enum class SportCategory(val status: String) {
    BEGINNER("beginner"),
    AMATEUR("amateur"),
    PROFESSIONAL("professional");

    companion object {
        fun ofStatus(status: String): SportCategory =
            values().firstOrNull { it.status == status }
                ?: throw IllegalArgumentException("Can't find sport category for status $status")
    }
}
