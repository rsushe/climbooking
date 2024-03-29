package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Booking @JsonCreator constructor(
    val id: Int?,
    @JsonProperty val climberId: Int,
    @JsonProperty val routeId: Int,
    var status: Status = Status.NEW,
    @JsonProperty val startTime: Instant,
    @JsonProperty val endTime: Instant,
) {
    fun active(): Booking = this.copy(status = Status.ACTIVE)

    enum class Status(
        private val cancellationAvailable: Boolean
    ) {
        NEW(false),
        UNAVAILABLE_DUE_TO_TOURNAMENT(false),
        CANCELLED(false),
        UNAVAILABLE_ROUTE_IS_ROLLED(false),
        ACTIVE(true),
        TOURNAMENT(true),
        FINISHED(false);
    }
}
