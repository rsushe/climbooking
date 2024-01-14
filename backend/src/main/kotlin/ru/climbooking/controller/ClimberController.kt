package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.dao.BookingDao
import ru.climbooking.domain.Climber
import ru.climbooking.dao.ClimberDao
import ru.climbooking.domain.ClimberRequest

@RestController
class ClimberController(private val climberDao: ClimberDao, private val bookingDao: BookingDao) {

    @GetMapping("/v1/climbers")
    fun getAllClimbers(): List<Climber> = climberDao.findAll()

    @PostMapping("/v1/climbers")
    fun insertClimber(@RequestBody climberRequest: ClimberRequest) = climberDao.insert(climberRequest)

    @GetMapping("/v1/climbers/{id}/bookings/active")
    fun getActiveBookingsForClimber(@PathVariable id: Int) = bookingDao.findActive(id)
}
