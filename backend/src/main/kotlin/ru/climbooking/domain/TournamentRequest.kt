package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class TournamentRequest(
    @JsonProperty val name: String,
    @JsonProperty val startDate: Instant,
    @JsonProperty val endDate: Instant,
    @JsonIgnore val status: String = "planned",
    @JsonProperty val organizerIds: List<Int>,
    @JsonProperty val routeIds: List<Int>
)
