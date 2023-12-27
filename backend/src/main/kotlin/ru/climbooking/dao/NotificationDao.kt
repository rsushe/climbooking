package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.SqlValue
import org.springframework.stereotype.Repository
import ru.climbooking.domain.Notification
import java.sql.PreparedStatement

@Repository
class NotificationDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
    fun insert(notification: Notification) {
        jdbcTemplate.update(
            "CALL create_notification(:climber_id, :booking_ids);",
            MapSqlParameterSource()
                .addValue("climber_id", notification.climberId)
                .addValue("booking_ids", IntegerArraySqlValue(notification.bookingIds.toTypedArray()))
        )
    }
}

private class IntegerArraySqlValue(
    private val array: Array<Int>
) : SqlValue {
    override fun setValue(ps: PreparedStatement, paramIndex: Int) {
        val arrayValue = ps.connection.createArrayOf(TYPE_NAME, array)
        ps.setArray(paramIndex, arrayValue)
    }

    override fun cleanup() {
        // empty
    }

    companion object {
        private val TYPE_NAME = "INTEGER"
    }
}
