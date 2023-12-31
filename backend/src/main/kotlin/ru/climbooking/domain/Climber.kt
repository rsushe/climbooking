package ru.climbooking.domain

import java.util.Date

data class Climber(
    val id: Int,
    val name: String,
    val birthday: Date,
    val sportCategory: SportCategory,
    val categoryName: String,
)
