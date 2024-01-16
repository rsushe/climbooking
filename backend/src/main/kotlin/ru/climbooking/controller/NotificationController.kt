package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.domain.Notification
import ru.climbooking.service.NotificationService

@RestController
class NotificationController(private val notificationService: NotificationService) {
    @PostMapping("/v1/notifications")
    fun createNotification(@RequestParam climberId: Int, @RequestParam bookingIds: List<Int>) {
        if (bookingIds.isEmpty()) {
            throw IllegalArgumentException("Booking ids cannot be empty")
        }

        notificationService.create(climberId, bookingIds)
    }

    @GetMapping("/v1/notifications")
    fun getNotifications(@RequestParam climberId: Int): NotificationListTo {
        return notificationService.get(climberId).toDto()
    }

    data class NotificationListTo(val notifications: List<Notification>)

    private fun List<Notification>.toDto() = NotificationListTo(this)
}
