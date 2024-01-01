package ru.climbooking.dao

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.climbooking.domain.Tournament
import ru.climbooking.domain.TournamentRequest
import ru.climbooking.domain.TournamentStatus
import ru.climbooking.exception.EntityNotFoundException
import java.sql.ResultSet

@Repository
class TournamentDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll(): List<Tournament> = jdbcTemplate.query(
        FIND_ALL,
    ) { rs, _ -> rs.unmap() }

    fun findById(tournamentId: Int): Tournament = try {
        jdbcTemplate.queryForObject(
            FIND_BY_ID,
            MapSqlParameterSource().addValue("tournament_id", tournamentId)
        ) { rs, _ -> rs.unmap() }!!
    } catch (emptyResultDataAccessException: EmptyResultDataAccessException) {
        throw EntityNotFoundException("Tournament with id $tournamentId does not exist", emptyResultDataAccessException)
    }

    fun findOngoingOrPlannedByRouteIds(routeIds: List<Int>): List<Tournament> = jdbcTemplate.query(
        FIND_ONGOING_OR_PLANNED_BY_ROUTE_IDS,
        MapSqlParameterSource().addValue("route_ids", routeIds.toTypedArray())
    ) { rs, _ -> rs.unmap() }

    @Transactional
    fun insert(tournament: TournamentRequest) = jdbcTemplate.update(
        "CALL create_tournament(:name, :start_date, :end_date, :status, :climber_ids, :route_ids)",
        MapSqlParameterSource()
            .addValue("name", tournament.name)
            .addValue("start_date", tournament.startDate)
            .addValue("end_date", tournament.endDate)
            .addValue("status", tournament.status)
            .addValue("climber_ids", tournament.organizersIds.toTypedArray())
            .addValue("route_ids", tournament.routeIds.toTypedArray())
    )

    companion object {
        private val FIND_ALL = """
            SELECT id, name, start_date, end_date, status
            FROM tournament
        """.trimIndent()
        private val FIND_BY_ID = """
            SELECT id, name, start_date, end_date, status
            FROM tournament
            WHERE id = :tournament_id
        """.trimIndent()
        private val FIND_ONGOING_OR_PLANNED_BY_ROUTE_IDS = """
            SELECT id, name, start_date, end_date, status
            FROM tournament
            INNER JOIN tournament_routes tr on tournament.id = tr.tournament_id
            WHERE status in ('ongoing', 'planned') and tr.route_id = any(:route_ids)
        """.trimIndent()
    }
}

private fun ResultSet.unmap() = Tournament(
    this.getInt("id"),
    this.getString("name"),
    this.getTimestamp("start_date").toInstant(),
    this.getTimestamp("end_date").toInstant(),
    TournamentStatus.ofStatus(this.getString("status"))
)
