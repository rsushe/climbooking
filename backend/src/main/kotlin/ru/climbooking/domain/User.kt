package ru.climbooking.domain

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val role: Role
)
