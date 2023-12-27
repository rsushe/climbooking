package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class ClimberRequest(
    @JsonProperty("name") val name: String,
    @JsonProperty("birthday") val birthday: Date,
    @JsonProperty("category") val category: String,
    @JsonProperty("category_id") val categoryId: UInt
)
