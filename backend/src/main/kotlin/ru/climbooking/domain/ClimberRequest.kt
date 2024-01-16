package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class ClimberRequest(
    @JsonProperty("name") val name: String,
    @JsonProperty("birthday") val birthday: Date,
    @JsonProperty("category_id") val categoryId: Int
)
