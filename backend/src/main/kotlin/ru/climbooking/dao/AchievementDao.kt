package ru.climbooking.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.climbooking.domain.Achievement

@Repository
class AchievementDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAll(): List<Achievement> = jdbcTemplate.query(
        FIND_ALL
    ) { rs, _ ->
        Entry(
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("climber_name")
        )
    }
        .groupBy({ AchievementInfo(it.title, it.description) }, { it.climberName })
        .map { Achievement(it.key.title, it.key.description, it.value) }

    companion object {
        private val FIND_ALL = """
            SELECT title, description, c.name as climber_name
            FROM achievement
            INNER JOIN climber_achievement ca on achievement.title = ca.achievement_title
            INNER JOIN climber c on c.id = ca.climber_id
        """.trimIndent()
    }
}

data class Entry(
    val title: String,
    val description: String,
    val climberName: String,
)

data class AchievementInfo(
    val title: String,
    val description: String,
)
