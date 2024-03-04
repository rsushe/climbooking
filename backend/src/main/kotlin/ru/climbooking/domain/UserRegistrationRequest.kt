package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRegistrationRequest(
    @JsonProperty val username: String,
    @JsonProperty val password: String,
    @JsonProperty val role: String,
)
