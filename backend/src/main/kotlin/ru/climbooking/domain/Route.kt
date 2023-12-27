package ru.climbooking.domain

import java.util.Date

data class Route(
    val id: Int,
    val placeId: Int,
    val name: String,
    val difficulty: Difficulty,
    val type: RouteType,
    val creationDate: Date,
    val isRolled: Boolean
)
