package ru.climbooking.domain

import java.time.Instant

data class Tournament(
    val id: Int,
    val name: String,
    val startDate: Instant,
    val endDate: Instant,
    val status: TournamentStatus,
    var organizers: List<Climber> = emptyList(),
    var routes: List<Route> = emptyList(),
)
