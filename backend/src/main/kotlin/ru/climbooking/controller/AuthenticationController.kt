package ru.climbooking.controller

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.domain.ClimbookingUserDetails
import ru.climbooking.domain.UserLoginRequest
import ru.climbooking.domain.UserRegistrationRequest
import ru.climbooking.service.ClimbookingUserDetailsService
import ru.climbooking.service.JwtService
import ru.climbooking.service.RegisterService

@RestController
class AuthenticationController(
    private val registerService: RegisterService,
    private val climbookingUserDetailsService: ClimbookingUserDetailsService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {

    @PostMapping("/v1/register")
    fun register(@RequestBody userRequest: UserRegistrationRequest) = registerService.registerNewUser(userRequest)

    @PostMapping("/v1/login")
    fun login(@RequestBody userRequest: UserLoginRequest): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(userRequest.username, userRequest.password)
        authenticationManager.authenticate(authenticationToken)

        val user = climbookingUserDetailsService.loadUserByUsername(userRequest.username) as ClimbookingUserDetails
        return jwtService.generateToken(user)
    }
}
