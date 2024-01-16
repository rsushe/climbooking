package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.dao.CategoryDao
import ru.climbooking.domain.Category

@RestController
class CategoryController(private val categoryDao: CategoryDao) {
    @GetMapping("/v1/categories")
    fun getAllCategories(): List<Category> = categoryDao.findAll()
}
