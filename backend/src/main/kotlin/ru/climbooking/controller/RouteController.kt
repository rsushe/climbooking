package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.dao.RouteDao
import ru.climbooking.domain.Route

@RestController
class RouteController(val routeDao: RouteDao) {
    @GetMapping("/v1/routes")
    fun getAllRoutes(): List<Route> = routeDao.findAll()

    @PatchMapping("/v1/route/roll/{routeId}")
    fun rollRoute(@PathVariable("routeId") routeId: Int) = routeDao.roll(routeId)
}
