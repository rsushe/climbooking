package ru.climbooking.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.climbooking.dao.BookingDao

@Service
class BookingCompletionService(private val bookingDao: BookingDao) {
    // routine will be executed every 10 seconds
    @Scheduled(fixedDelay = 10000)
    fun complete() {
        bookingDao.updateExpiredEndDate()
    }
}
