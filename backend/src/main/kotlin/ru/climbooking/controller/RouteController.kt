package ru.climbooking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.dao.RouteDao
import ru.climbooking.domain.Route
import ru.climbooking.domain.RouteRequest

@RestController
class RouteController(val routeDao: RouteDao) {
    @GetMapping("/v1/routes")
    fun getAllRoutes(): List<Route> = routeDao.findAll()

    @PostMapping("/v1/route")
    fun addRoute(@RequestBody routeRequest: RouteRequest) = routeDao.insert(routeRequest)

    @PatchMapping("/v1/route/roll/{routeId}")
    fun rollRoute(@PathVariable("routeId") routeId: Int) = routeDao.roll(routeId)
}
