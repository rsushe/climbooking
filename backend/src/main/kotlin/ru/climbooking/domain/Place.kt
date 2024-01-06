package ru.climbooking.domain

import org.postgresql.geometric.PGpoint

data class Place(
    val id: Int,
    val name: String,
    val type: String,
    val location: PGpoint
)
