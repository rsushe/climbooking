package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class RouteRequest(
    @JsonProperty val placeId: Int,
    @JsonProperty val name: String,
    @JsonProperty val difficulty: String,
    @JsonProperty val type: String,
    @JsonProperty val creationDate: Date,
    @JsonProperty val authors: List<Int>
)
