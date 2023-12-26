package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.domain.Climber
import ru.climbooking.service.ClimberDao

@RestController
class ClimberController(private val climberDao: ClimberDao) {

    @GetMapping("/v1/climbers")
    fun getAllClimbers(): List<Climber> = climberDao.findAll()
}
