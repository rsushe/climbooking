package ru.climbooking.domain

import java.time.Instant

data class Booking(
    val id: UInt,
    val climberId: UInt,
    val routeId: UInt,
    val status: Status,
    val startTime: Instant,
    val endTime: Instant,
) {
    enum class Status {
        UNAVAILABLE_DUE_TO_TOURNAMENT,
        CANCELLED,
        UNAVAILABLE_ROUTE_IS_ROLLED,
        ACTIVE,
    }
}
