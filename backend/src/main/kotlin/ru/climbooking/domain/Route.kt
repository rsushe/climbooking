package ru.climbooking.domain

import java.util.Date

data class Route(
    val id: Int,
    val placeId: Int,
    val name: String,
    val difficulty: String,
    val type: RouteType,
    val creationDate: Date,
    val isRolled: Boolean
)
