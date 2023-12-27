package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.climbooking.domain.Climber
import ru.climbooking.domain.ClimberRequest
import ru.climbooking.domain.SportCategory
import java.sql.Types

@Repository
class ClimberDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll(): List<Climber> = jdbcTemplate.query(
        SELECT_ALL
    ) { rs, _ ->
        Climber(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDate("birthday"),
            SportCategory.ofStatus(rs.getString("sport_category")),
            rs.getString("category_name")
        )
    }

    @Transactional
    fun insert(climber: ClimberRequest) {
        println(climber)
        jdbcTemplate.query(
            CALL_INSERT,
            MapSqlParameterSource()
                .addValue("climber_name", climber.name)
                .addValue("birthday", climber.birthday, Types.DATE)
                .addValue("sport_category", climber.category)
                .addValue("category_id", climber.categoryId.toInt())
        ) {}
    }

    companion object {
        private const val CALL_INSERT = "SELECT add_climber(:climber_name, :birthday, :sport_category, :category_id);"
        private val SELECT_ALL = """
            select cl.id, cl.name, cl.birthday, cl.sport_category, ct.name as category_name
            from climber cl
            inner join category ct on cl.category_id = ct.id
        """.trimIndent()
    }
}
