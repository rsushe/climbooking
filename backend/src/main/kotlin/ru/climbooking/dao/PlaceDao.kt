package ru.climbooking.dao

import org.postgresql.geometric.PGpoint
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.climbooking.domain.Place

@Repository
class PlaceDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll(): List<Place> = jdbcTemplate.query(
        FIND_ALL
    ) { rs, _ ->
        Place(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getObject("location") as PGpoint
        )
    }

    companion object {
        private val FIND_ALL = """
            SELECT id, name, type, location
            FROM place
        """.trimIndent()
    }
}
