package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.climbooking.domain.Route
import ru.climbooking.domain.RouteRequest
import ru.climbooking.domain.RouteType
import java.lang.IllegalArgumentException
import java.sql.ResultSet

@Repository
class RouteDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll(): List<Route> = jdbcTemplate.query(
        FIND_ALL,
    ) { rs, _ -> rs.unmap() }

    fun findById(routeId: Int): Route = jdbcTemplate.queryForObject(
        FIND_BY_ID,
        MapSqlParameterSource().addValue("route_id", routeId)
    ) { rs, _ -> rs.unmap() }!!

    fun findAllTournamentRoutes(tournamentId: Int): List<Route> = jdbcTemplate.query(
        FIND_ALL_TOURNAMENT_ROUTES,
        MapSqlParameterSource().addValue("tournament_id", tournamentId)
    ) { rs, _ -> rs.unmap() }

    fun findAllRouteDifficulties(): List<String> = jdbcTemplate.query(
        FIND_ALL_ROUTE_DIFFICULTIES
    ) { rs, _ -> rs.getString("difficulty") }

    @Transactional
    fun roll(routeId: Int) {
        if (isRouteRolled(routeId)) {
            throw IllegalArgumentException("Route $routeId is already rolled")
        }
        jdbcTemplate.update(
            ROLL_ROUTE_BY_ID,
            MapSqlParameterSource().addValue("route_id", routeId)
        )
    }

    fun isRouteRolled(routeId: Int) = jdbcTemplate.queryForObject(
        GET_IS_ROLLED_BY_ID,
        MapSqlParameterSource().addValue("route_id", routeId),
        Boolean::class.java
    )!!

    @Transactional
    fun insert(routeRequest: RouteRequest) = jdbcTemplate.update(
        "CALL create_route(:place_id, :climber_ids, :name, :difficulty, :type, :creation_date);",
        MapSqlParameterSource()
            .addValue("place_id", routeRequest.placeId)
            .addValue("climber_ids", routeRequest.authors.toTypedArray())
            .addValue("name", routeRequest.name)
            .addValue("difficulty", routeRequest.difficulty)
            .addValue("type", routeRequest.type)
            .addValue("creation_date", routeRequest.creationDate)
    )

    companion object {
        private val FIND_ALL = """
            SELECT id, place_id, name, difficulty, type, creation_date, is_rolled
            FROM route
        """.trimIndent()
        private val FIND_BY_ID = """
            SELECT id, place_id, name, difficulty, type, creation_date, is_rolled
            FROM route
            WHERE id = :route_id
        """.trimIndent()
        private val FIND_ALL_TOURNAMENT_ROUTES = """
            SELECT id, place_id, name, difficulty, type, creation_date, is_rolled
            FROM route
            INNER JOIN tournament_routes tr on route.id = tr.route_id
            WHERE tr.tournament_id = :tournament_id
        """.trimIndent()
        private val FIND_ALL_ROUTE_DIFFICULTIES = """
            SELECT unnest(enum_range(NULL::difficulty_enum)) AS difficulty
        """.trimIndent()
        private val GET_IS_ROLLED_BY_ID = """
            SELECT is_rolled
            FROM route
            WHERE id = :route_id
        """.trimIndent()
        private val ROLL_ROUTE_BY_ID = """
            UPDATE route
            SET is_rolled = true
            WHERE id = :route_id
        """.trimIndent()
    }
}

private fun ResultSet.unmap() = Route(
    this.getInt("id"),
    this.getInt("place_id"),
    this.getString("name"),
    this.getString("difficulty"),
    RouteType.ofRepresentation(this.getString("type")),
    this.getDate("creation_date"),
    this.getBoolean("is_rolled")
)
