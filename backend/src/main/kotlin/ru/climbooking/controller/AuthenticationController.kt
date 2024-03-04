package ru.climbooking.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.domain.ClimbookingUserDetails
import ru.climbooking.domain.Role
import ru.climbooking.domain.UserLoginRequest
import ru.climbooking.domain.UserRegistrationRequest
import ru.climbooking.exception.EntityNotFoundException
import ru.climbooking.service.ClimbookingUserDetailsService
import ru.climbooking.service.JwtService
import ru.climbooking.service.RegisterService

@RestController
@RequestMapping("v1/authentication")
class AuthenticationController(
    private val registerService: RegisterService,
    private val climbookingUserDetailsService: ClimbookingUserDetailsService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {

    @GetMapping("/roles")
    fun roles() = Role.entries.map { it.roleName }

    @PostMapping("/register")
    fun register(@RequestBody userRequest: UserRegistrationRequest) = registerService.registerNewUser(userRequest)

    @PostMapping("/login")
    fun login(@RequestBody userRequest: UserLoginRequest): ResponseEntity<String> {
        val authenticationToken = UsernamePasswordAuthenticationToken(userRequest.username, userRequest.password)
        authenticationManager.authenticate(authenticationToken)

        try {
            val user = climbookingUserDetailsService.loadUserByUsername(userRequest.username) as ClimbookingUserDetails
            return ResponseEntity.ok(jwtService.generateToken(user))
        } catch (e: EntityNotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }
}
