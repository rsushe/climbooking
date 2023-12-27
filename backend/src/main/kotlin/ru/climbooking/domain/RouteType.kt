package ru.climbooking.domain

import java.lang.IllegalArgumentException

enum class RouteType(private val representation: String) {
    BOULDERING("bouldering"),
    LEAD("lead");

    companion object {
        fun ofRepresentation(representation: String): RouteType =
            RouteType.values().firstOrNull { it.representation == representation }
                ?: throw IllegalArgumentException("Can't find sport route type for representation $representation")
    }
}
