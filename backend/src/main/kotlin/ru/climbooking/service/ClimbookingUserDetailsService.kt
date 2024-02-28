package ru.climbooking.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.climbooking.dao.UserDao
import ru.climbooking.domain.ClimbookingUserDetails

@Service
class ClimbookingUserDetailsService(private val userDao: UserDao) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        ClimbookingUserDetails(userDao.findByUsername(username))
}
