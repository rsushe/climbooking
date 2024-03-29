package ru.climbooking.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.climbooking.domain.Tournament
import ru.climbooking.domain.TournamentRequest
import ru.climbooking.service.TournamentService

@RestController
class TournamentController(private val tournamentService: TournamentService) {

    @GetMapping("/v1/tournaments")
    fun getAllTournaments(): List<Tournament> = tournamentService.findAll()

    @GetMapping("/v1/tournaments/{tournamentId}")
    fun getTournamentFullInfo(@PathVariable("tournamentId") tournamentId: Int): Tournament =
        tournamentService.getTournamentFullInfo(tournamentId)

    @PostMapping("/v1/tournaments")
    fun createTournament(@RequestBody tournamentRequest: TournamentRequest): ResponseEntity<List<Tournament>> {
        val overlayingTournaments = tournamentService.createTournament(tournamentRequest)

        if (overlayingTournaments.isNotEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(overlayingTournaments)
        }

        return ResponseEntity.ok().build()
    }
}
