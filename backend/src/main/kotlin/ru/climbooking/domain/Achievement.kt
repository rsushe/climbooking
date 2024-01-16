package ru.climbooking.domain

data class Achievement(
    val title: String,
    val description: String,
    val ownersNames: List<String>
)
