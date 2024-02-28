package ru.climbooking.domain

import java.lang.IllegalArgumentException

enum class Role(val roleName: String) {
    CLIMBER("climber"),
    ADMIN("admin");

    companion object {
        fun ofStatus(roleName: String): Role =
            Role.values().firstOrNull { it.roleName == roleName }
                ?: throw IllegalArgumentException("Can't find role for status $roleName")
    }
}
