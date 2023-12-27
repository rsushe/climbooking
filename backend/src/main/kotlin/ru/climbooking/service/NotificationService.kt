package ru.climbooking.service

import org.springframework.stereotype.Service
import ru.climbooking.dao.NotificationDao
import ru.climbooking.domain.Notification

@Service
class NotificationService(private val notificationDao: NotificationDao) {
    fun create(climberId: Int, bookingIds: List<Int>) {
        notificationDao.insert(Notification(climberId = climberId, bookingIds = bookingIds))
    }
}