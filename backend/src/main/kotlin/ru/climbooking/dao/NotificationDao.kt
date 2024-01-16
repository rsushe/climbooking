package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.climbooking.domain.Notification
import java.sql.ResultSet

@Repository
class NotificationDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    @Transactional
    fun insert(notification: Notification) {
        jdbcTemplate.update(
            "CALL create_notification(:climber_id, :booking_ids);",
            MapSqlParameterSource()
                .addValue("climber_id", notification.climberId)
                .addValue("booking_ids", notification.bookingIds.toTypedArray())
        )
    }

    fun find(climberId: Int): List<Notification> =
        jdbcTemplate.query(
            "SELECT id, climber_id, status, route_id FROM notification WHERE climber_id = :climber_id;",
            MapSqlParameterSource().addValue("climber_id", climberId)
        ) { rs: ResultSet, _: Int ->
            Notification(
                rs.getInt("id"),
                rs.getInt("climber_id"),
                Notification.Status.valueOf(rs.getString("status")),
                if (rs.getInt("route_id") == 0) null else rs.getInt("route_id")
            )
        }
}
