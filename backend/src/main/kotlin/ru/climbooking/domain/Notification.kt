package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonIgnore

data class Notification(
    val id: Int? = null,
    val climberId: Int,
    val status: Status = Status.NEW,
    @JsonIgnore
    val bookingIds: List<Int> = emptyList(),
) {
    enum class Status {
        NEW,
        BOOKING_IS_AVAILABLE,
        UNAVAILABLE_BECAUSE_ROUTE_IS_ROLLED,
        CANCELLED_BECAUSE_OF_TOURNAMENT,
    }
}
