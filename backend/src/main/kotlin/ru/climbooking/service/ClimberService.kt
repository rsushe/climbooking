package ru.climbooking.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import ru.climbooking.model.Climber
import ru.climbooking.model.SportCategory

@Service
class ClimberService(private val jdbcTemplate: JdbcTemplate) {

    fun getAllClimbers(): List<Climber> = jdbcTemplate.query(
        """
            select cl.id, cl.name, cl.birthday, cl.sport_category, ct.name as category_name
            from climber cl
            inner join category ct on cl.category_id = ct.id
        """.trimIndent(),
    ) { rs, _ ->
        Climber(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDate("birthday"),
            SportCategory.ofStatus(rs.getString("sport_category")),
            rs.getString("category_name")
        )
    }
}
