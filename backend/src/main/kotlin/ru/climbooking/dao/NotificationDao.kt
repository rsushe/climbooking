package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.climbooking.domain.Notification

@Repository
class NotificationDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun insert(notification: Notification) {
        jdbcTemplate.update(
            "CALL create_notification(:climber_id, :booking_ids);",
            MapSqlParameterSource()
                .addValue("climber_id", notification.climberId)
                .addValue("booking_ids", notification.bookingIds.toTypedArray())
        )
    }
}
