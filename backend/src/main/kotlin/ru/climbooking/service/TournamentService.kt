package ru.climbooking.service

import org.springframework.stereotype.Service
import ru.climbooking.dao.ClimberDao
import ru.climbooking.dao.RouteDao
import ru.climbooking.dao.TournamentDao
import ru.climbooking.domain.Tournament
import ru.climbooking.domain.TournamentRequest

@Service
class TournamentService(
    private val tournamentDao: TournamentDao,
    private val climberDao: ClimberDao,
    private val routeDao: RouteDao
) {

    fun findAll(): List<Tournament> = tournamentDao.findAll()

    fun getTournamentFullInfo(tournamentId: Int): Tournament {
        val tournament = tournamentDao.findById(tournamentId)
        tournament.organizers = climberDao.findAllTournamentOrganizers(tournamentId)
        tournament.routes = routeDao.findAllTournamentRoutes(tournamentId)

        return tournament
    }

    fun createTournament(tournamentRequest: TournamentRequest) {
        if (tournamentRequest.organizerIds.isEmpty() || tournamentRequest.routeIds.isEmpty()) {
            throw IllegalArgumentException("Organizers and routes cannot be empty")
        }

        val overlayingTournaments = tournamentDao.findOngoingOrPlannedByRouteIds(tournamentRequest.routeIds).filter {
            (it.startDate >= tournamentRequest.startDate && it.startDate < tournamentRequest.endDate)
                || (it.endDate > tournamentRequest.startDate && it.endDate <= tournamentRequest.endDate)
        }

        if (overlayingTournaments.isNotEmpty()) {
            throw IllegalArgumentException("There is overlaying tournaments: $overlayingTournaments")
        }

        tournamentDao.insert(tournamentRequest)
    }
}
