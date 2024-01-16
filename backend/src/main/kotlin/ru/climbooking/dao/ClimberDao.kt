package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.climbooking.domain.Climber
import ru.climbooking.domain.ClimberRequest
import java.sql.ResultSet
import java.sql.Types

@Repository
class ClimberDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll(): List<Climber> = jdbcTemplate.query(
        FIND_ALL
    ) { rs, _ -> rs.unmap() }

    fun findAllTournamentOrganizers(tournamentId: Int): List<Climber> = jdbcTemplate.query(
        FIND_ALL_TOURNAMENT_ORGANIZERS,
        MapSqlParameterSource().addValue("tournament_id", tournamentId)
    ) { rs, _ -> rs.unmap() }

    @Transactional
    fun insert(climber: ClimberRequest) {
        jdbcTemplate.update(
            "CALL add_climber(:climber_name, :birthday, :category_id);",
            MapSqlParameterSource()
                .addValue("climber_name", climber.name)
                .addValue("birthday", climber.birthday, Types.DATE)
                .addValue("category_id", climber.categoryId)
        )
    }

    companion object {
        private val FIND_ALL = """
            SELECT cl.id, cl.name, cl.birthday, cl.sport_category, ct.name as category_name
            FROM climber cl
            INNER JOIN category ct on cl.category_id = ct.id
        """.trimIndent()
        private val FIND_ALL_TOURNAMENT_ORGANIZERS = """
            SELECT cl.id, cl.name, cl.birthday, cl.sport_category, ct.name as category_name
            FROM climber cl
            INNER JOIN category ct on cl.category_id = ct.id
            INNER JOIN tournament_organisator t on t.climber_id = cl.id
            WHERE t.tournament_id = :tournament_id
        """.trimIndent()
    }
}

private fun ResultSet.unmap() = Climber(
    this.getInt("id"),
    this.getString("name"),
    this.getDate("birthday"),
    this.getString("category_name")
)
