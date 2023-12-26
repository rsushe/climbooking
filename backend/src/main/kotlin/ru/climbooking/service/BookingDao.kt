package ru.climbooking.service

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
        jdbcTemplate.update(
            CALL_INSERT,
            MapSqlParameterSource()
                .addValue("climber_id", booking.climberId)
                .addValue("route_id", booking.routeId)
                .addValue("status", booking.status)
                .addValue("start_time", Timestamp.from(booking.startTime), Types.TIMESTAMP)
                .addValue("end_time", Timestamp.from(booking.endTime), Types.TIMESTAMP)
        )
    }

    fun findAll(): List<Booking> = jdbcTemplate.query(
        FIND_ALL
    ) { rs: ResultSet, _:Int ->
        Booking(
            rs.getInt("id").toUInt(),
            rs.getInt("climber_id").toUInt(),
            rs.getInt("route_id").toUInt(),
            Booking.Status.valueOf(rs.getString("status")),
            rs.getTimestamp("start_time").toInstant(),
            rs.getTimestamp("end_time").toInstant(),
        )
    }

    companion object {
        private val CALL_INSERT = "SELECT add_booking(:climber_id, :route_id, :status, :start_time, :end_time);"
        private val FIND_ALL = """
            SELECT id, climber_id, route_id, status, start_time, end_time FROM booking;
        """.trimIndent()
    }
}