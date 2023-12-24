package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.model.Climber
import ru.climbooking.service.ClimberService

@RestController
class ClimberController(private val climberService: ClimberService) {

    @GetMapping("/climbers")
    fun getAllClimbers(): List<Climber> = climberService.getAllClimbers()
}
