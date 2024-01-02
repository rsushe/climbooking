package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class TournamentRequest(
    @JsonProperty val name: String,
    @JsonProperty val startDate: Instant,
    @JsonProperty val endDate: Instant,
    @JsonProperty val status: String,
    @JsonProperty val organizerIds: List<Int>,
    @JsonProperty val routeIds: List<Int>
)
