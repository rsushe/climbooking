package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.dao.PlaceDao
import ru.climbooking.domain.Place

@RestController
class PlaceController(private val placeDao: PlaceDao) {

    @GetMapping("/v1/places")
    fun getAllPlaces(): List<Place> = placeDao.findAll()
}
