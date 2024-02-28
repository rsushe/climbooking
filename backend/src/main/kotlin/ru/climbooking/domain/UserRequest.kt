package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRequest(
    @JsonProperty val username: String,
    @JsonProperty val password: String,
    @JsonProperty val role: String,
)
