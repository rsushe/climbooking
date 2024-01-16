package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.dao.AchievementDao
import ru.climbooking.domain.Achievement

@RestController
class AchievementController(private val achievementDao: AchievementDao) {

    @GetMapping("/v1/achievements")
    fun getAllAchievements(): List<Achievement> = achievementDao.findAll()
}
