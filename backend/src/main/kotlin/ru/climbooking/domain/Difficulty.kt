package ru.climbooking.domain

import java.lang.IllegalArgumentException

enum class Difficulty(private val representation: String) {
    SIX_A("6a"),
    SIX_B("6b"),
    SIX_C("6c");

    companion object {
        fun ofRepresentation(representation: String): Difficulty =
            Difficulty.values().firstOrNull { it.representation == representation }
                ?: throw IllegalArgumentException("Can't find sport difficulty for representation $representation")
    }
}
