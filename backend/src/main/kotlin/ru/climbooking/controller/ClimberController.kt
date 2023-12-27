package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.domain.Climber
import ru.climbooking.dao.ClimberDao
import ru.climbooking.domain.ClimberRequest

@RestController
class ClimberController(private val climberDao: ClimberDao) {

    @GetMapping("/v1/climbers")
    fun getAllClimbers(): List<Climber> = climberDao.findAll()

    @PostMapping("/v1/climber")
    fun insertClimber(@RequestBody climberRequest: ClimberRequest) = climberDao.insert(climberRequest)
}
