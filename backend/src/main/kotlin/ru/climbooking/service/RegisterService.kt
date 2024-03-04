package ru.climbooking.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.climbooking.dao.UserDao
import ru.climbooking.domain.UserRegistrationRequest
import java.lang.IllegalArgumentException

@Service
class RegisterService(private val userDao: UserDao, private val passwordEncoder: PasswordEncoder) {

    fun registerNewUser(userRequest: UserRegistrationRequest) {
        if (userDao.existsByUsername(userRequest.username)) {
            throw IllegalArgumentException("User with this username already exists")
        }

        val user = UserRegistrationRequest(
            username = userRequest.username,
            password = passwordEncoder.encode(userRequest.password),
            role = userRequest.role,
        )

        userDao.save(user)
    }
}
