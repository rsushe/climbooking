package ru.climbooking.domain

data class Notification(
    val id: Int? = null,
    val climberId: Int,
    val status: Status = Status.NEW,
    val bookingIds: List<Int>,
) {
    enum class Status {
        NEW,
        BOOKING_IS_AVAILABLE,
        UNAVAILABLE_BECAUSE_ROUTE_IS_ROLLED,
        CANCELLED_BECAUSE_OF_TOURNAMENT,
    }
}
