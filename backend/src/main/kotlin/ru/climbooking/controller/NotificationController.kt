package ru.climbooking.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.service.NotificationService

@RestController
class NotificationController(private val notificationService: NotificationService) {
    @PostMapping("/v1/notifications")
    fun createNotification(@RequestParam climberId: Int, @RequestParam bookingIds: List<Int>) {
        notificationService.create(climberId, bookingIds)
    }
}