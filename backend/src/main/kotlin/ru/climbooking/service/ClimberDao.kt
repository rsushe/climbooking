package ru.climbooking.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.climbooking.domain.Climber
import ru.climbooking.domain.SportCategory

@Repository
class ClimberDao(private val jdbcTemplate: JdbcTemplate) {

    fun findAll(): List<Climber> = jdbcTemplate.query(
        SELECT_ALL
    ) { rs, _ ->
        Climber(
            rs.getInt("id").toUInt(),
            rs.getString("name"),
            rs.getDate("birthday"),
            SportCategory.ofStatus(rs.getString("sport_category")),
            rs.getString("category_name")
        )
    }

    companion object {
        private val SELECT_ALL = """
            select cl.id, cl.name, cl.birthday, cl.sport_category, ct.name as category_name
            from climber cl
            inner join category ct on cl.category_id = ct.id
        """.trimIndent()
    }
}
