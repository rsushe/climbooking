package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class RouteRequest(
    @JsonProperty("place_id") val placeId: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("difficulty") val difficulty: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("creation_date") val creationDate: Date,
)