package ru.climbooking.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class UserLoginRequest(
    @JsonProperty val username: String,
    @JsonProperty val password: String,
)
