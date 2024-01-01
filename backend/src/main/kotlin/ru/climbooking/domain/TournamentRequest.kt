package ru.climbooking.domain

import java.time.Instant

data class TournamentRequest(
    val name: String,
    val startDate: Instant,
    val endDate: Instant,
    val status: TournamentStatus,
    val organizersIds: List<Int>,
    val routeIds: List<Int>
)
