package ru.climbooking.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.domain.Booking
import ru.climbooking.service.BookingService

@RestController
class BookingController(private val bookingService: BookingService) {
    @PostMapping("/v1/bookings")
    fun createBooking(@RequestBody booking: Booking): ResponseEntity<BookingListTo> {
        val overlayingBookings: List<Booking> = bookingService.create(booking)

        if (overlayingBookings.isNotEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(overlayingBookings.toDto())
        }

        return ResponseEntity.ok().build()
    }

    @GetMapping("/v1/bookings")
    fun getAllBookings(): BookingListTo {
        return bookingService.getAll().toDto()
    }

    @GetMapping("/v1/bookings/active")
    fun getActiveBookings(@RequestParam routeId: Int): BookingListTo {
        return bookingService.getActive(routeId).toDto()
    }

    @PostMapping("/v1/bookings/{id}/cancellation")
    fun cancelBooking(@PathVariable("id") bookingId: Int) {
        bookingService.cancel(bookingId)
    }

    data class BookingListTo(
        val bookings: List<Booking>
    )

    private fun List<Booking>.toDto() = BookingListTo(this)
}
