package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.climbooking.domain.Booking
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types

@Repository
class BookingDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    @Transactional
    fun insert(booking: Booking) {
        jdbcTemplate.query(
            CALL_INSERT,
            MapSqlParameterSource()
                .addValue("climber_id", booking.climberId)
                .addValue("route_id", booking.routeId)
                .addValue("status", booking.status.name)
                .addValue("start_time", Timestamp.from(booking.startTime), Types.TIMESTAMP)
                .addValue("end_time", Timestamp.from(booking.endTime), Types.TIMESTAMP)
        ) {}
    }

    @Transactional
    fun updateStatus(bookingId: Int, newStatus: Booking.Status) {
        jdbcTemplate.update(
            "UPDATE booking SET status = :new_status WHERE id = :id;",
            MapSqlParameterSource()
                .addValue("id", bookingId)
                .addValue("new_status", newStatus.name)
        )
    }

    fun findAll(): List<Booking> = jdbcTemplate.query(
        FIND_ALL
    ) { rs: ResultSet, _: Int -> rs.unmap() }

    fun findActiveOrTournament(routeId: Int): List<Booking> = jdbcTemplate.query(
        FIND_ACTIVE_OR_TOURNAMENT_BY_ROUTE_ID,
        MapSqlParameterSource().addValue("route_id", routeId)
    ) { rs: ResultSet, _: Int -> rs.unmap() }

    companion object {
        private const val CALL_INSERT = "SELECT add_booking(:climber_id, :route_id, :status, :start_time, :end_time);"
        private val FIND_ALL = """
            SELECT id, climber_id, route_id, status, start_time, end_time FROM booking;
        """.trimIndent()
        private val FIND_ACTIVE_OR_TOURNAMENT_BY_ROUTE_ID = """
            SELECT id, climber_id, route_id, status, start_time, end_time 
            FROM booking
            WHERE status in ('ACTIVE', 'TOURNAMENT') and route_id = :route_id;
        """.trimIndent()
    }
}

private fun ResultSet.unmap() = Booking(
    this.getInt("id"),
    this.getInt("climber_id"),
    this.getInt("route_id"),
    Booking.Status.valueOf(this.getString("status")),
    this.getTimestamp("start_time").toInstant(),
    this.getTimestamp("end_time").toInstant(),
)
