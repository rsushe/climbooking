package ru.climbooking.domain

enum class SportCategory(val status: String) {
    BEGINNER("beginner"),
    AMATEUR("amateur"),
    PROFESSIONAL("professional");

    companion object {
        fun ofStatus(status: String): SportCategory = values().first { it.status == status }
    }
}
