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
    fun getAllRoutes(): List<Route> = routeDao.findAll().sortedBy { it.id }

    @GetMapping("/v1/routes/{routeId}")
    fun getRouteById(@PathVariable("routeId") routeId: Int): Route = routeDao.findById(routeId)

    @GetMapping("/v1/routes/difficulties")
    fun getAllRoutesDifficulties(): List<String> = routeDao.findAllRouteDifficulties()

    @PostMapping("/v1/routes")
    fun addRoute(@RequestBody routeRequest: RouteRequest) = routeDao.insert(routeRequest)

    @PatchMapping("/v1/routes/{routeId}/roll")
    fun rollRoute(@PathVariable("routeId") routeId: Int) = routeDao.roll(routeId)
}
