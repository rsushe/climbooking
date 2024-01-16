package ru.climbooking.controller

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.dao.BookingDao
import ru.climbooking.dao.ClimberDao
import ru.climbooking.domain.Booking
import ru.climbooking.domain.Climber
import ru.climbooking.domain.ClimberRequest

@RestController
class ClimberController(private val climberDao: ClimberDao, private val bookingDao: BookingDao) {

    @GetMapping("/v1/climbers")
    fun getAllClimbers(): List<Climber> = climberDao.findAll()

    @PostMapping("/v1/climbers")
    fun insertClimber(@RequestBody climberRequest: ClimberRequest): ResponseEntity<Message> {
        try {
            climberDao.insert(climberRequest)
            return ResponseEntity.ok().build()
        } catch (e: DataIntegrityViolationException) {
            return ResponseEntity.badRequest().body(Message("Birthday must be in the past"))
        }
    }

    @GetMapping("/v1/climbers/{id}/bookings/active")
    fun getActiveBookingsForClimber(@PathVariable id: Int) = bookingDao.findActive(id)

    data class Message(
        val message: String
    )
}
