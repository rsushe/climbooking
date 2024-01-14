package ru.climbooking.service

import org.springframework.stereotype.Service
import ru.climbooking.dao.BookingDao
import ru.climbooking.dao.RouteDao
import ru.climbooking.domain.Booking

@Service
class BookingService(
    private val bookingDao: BookingDao,
    private val routeDao: RouteDao,
) {
    fun create(booking: Booking): List<Booking> {
        if (routeDao.isRouteRolled(booking.routeId)) {
            throw IllegalArgumentException("Route is rolled, booking cannot be created")
        }

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
