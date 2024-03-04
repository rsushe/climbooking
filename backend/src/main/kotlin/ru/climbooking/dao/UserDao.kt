package ru.climbooking.dao

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.climbooking.domain.Role
import ru.climbooking.domain.User
import ru.climbooking.domain.UserRegistrationRequest
import ru.climbooking.exception.EntityNotFoundException

@Repository
class UserDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun existsByUsername(username: String): Boolean = jdbcTemplate.queryForObject(
        """
            select count(*)
            from "user"
            where username = :username
        """.trimIndent(),
        MapSqlParameterSource().addValue("username", username),
        Int::class.java
    )!! > 0

    fun findByUsername(username: String): User = try {
        jdbcTemplate.queryForObject(
            """
            select id, username, password, role
            from "user"
            where username = :username
        """.trimIndent(),
            MapSqlParameterSource().addValue("username", username)
        ) { rs, _ ->
            User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                Role.ofStatus(rs.getString("role"))
            )
        }!!
    } catch (e: EmptyResultDataAccessException) {
        throw EntityNotFoundException("User with username $username does not exists", e)
    }

    @Transactional
    fun save(userRequest: UserRegistrationRequest) = jdbcTemplate.update(
        """
            insert into "user"(username, password, role)
            values (:username, :password, :role)
        """.trimIndent(),
        MapSqlParameterSource()
            .addValue("username", userRequest.username)
            .addValue("password", userRequest.password)
            .addValue("role", userRequest.role)
    )
}
