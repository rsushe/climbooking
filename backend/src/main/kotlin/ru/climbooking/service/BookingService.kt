package ru.climbooking.service

import org.springframework.stereotype.Service
import ru.climbooking.dao.BookingDao
import ru.climbooking.domain.Booking

@Service
class BookingService(private val bookingDao: BookingDao) {
    fun create(booking: Booking): List<Booking> {
        val overlayingBookings: List<Booking> = bookingDao.findActiveOrTournament(booking.routeId).filter {
            (it.startTime >= booking.startTime && it.startTime < booking.endTime)
                || (it.endTime > booking.startTime && it.endTime <= booking.endTime)
        }

        if (overlayingBookings.isEmpty()) {
            bookingDao.insert(booking.active())
            return emptyList()
        }

        return overlayingBookings
    }

    fun cancel(bookingId: Int) {
        bookingDao.updateStatus(bookingId, Booking.Status.CANCELLED)
    }

    fun getAll(): List<Booking> = bookingDao.findAll()

    fun getActive(routeId: Int): List<Booking> = bookingDao.findActiveOrTournament(routeId)
}
