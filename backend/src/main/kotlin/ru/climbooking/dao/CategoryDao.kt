package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.climbooking.domain.Category

@Repository
class CategoryDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun findAll(): List<Category> = jdbcTemplate.query(
        "SELECT id, from_age, to_age, name FROM category;"
    ) { rs, _ ->
        Category(
            rs.getInt("id"),
            rs.getInt("from_age"),
            rs.getInt("to_age"),
            rs.getString("name")
        )
    }
}