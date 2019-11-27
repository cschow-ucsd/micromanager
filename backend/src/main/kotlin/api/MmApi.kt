package api

import call.MmProblemRequest
import call.MmSolveStatus
import call.OpPID
import call.OpPIDs
import exposed.dao.MmSolutionEvent
import exposed.dao.MmUser
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import optaplanner.EventSchedule
import org.jetbrains.exposed.sql.transactions.transaction
import org.optaplanner.core.api.solver.SolverFactory
import util.findSolution
import util.getSolutionStatuses
import util.mmSession
import util.solve

private val runningOpPIDs: MutableList<Pair<MmUser, MmSolveStatus>> = mutableListOf()
private val opSolverFactory: SolverFactory<EventSchedule> = SolverFactory
        .createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")

/**
 * Public routes.
 * Can be accessed by directly calling the endpoints.
 */
@KtorExperimentalAPI
fun Route.mmPublicApi() {
    get("/") {
        call.respondText("Hello World!")
    }
}

/**
 * Protected routes.
 * Can only be accessed if session exists or logged in.
 */
fun Route.mmProtectedApi() = authenticate(MmAuthenticate.API_AUTH) {
    route("/api") {
        get("/login") {
            call.handleSession()
            call.respond(HttpStatusCode.OK)
        }
        post("/op-solve") {
            call.handleSession()
            val mmUser = transaction { MmUser[call.mmSession!!.subject] }
            val problem = call.receive<MmProblemRequest>()
            val mmSolveStatus = problem.solve(opSolverFactory.buildSolver(), mmUser) {
                runningOpPIDs += mmUser to it
                call.respond(HttpStatusCode.Accepted, it)
            }
            runningOpPIDs.removeIf { it.second.pid == mmSolveStatus.pid }
        }
        route("/status") {
            get("/all") {
                call.handleSession()
                val mmUser = transaction { MmUser[call.mmSession!!.subject] }
                val userOpPIDs: OpPIDs = mmUser.opSolutionEvents.map { it.opPID }
                val done = transaction { MmSolutionEvent.getSolutionStatuses(mmUser, userOpPIDs) }
                val running = mutableListOf<MmSolveStatus>()
                runningOpPIDs.forEach {
                    if (it.first.id == mmUser.id) running.add(it.second)
                }
                val statusResponse: List<MmSolveStatus> = done + running
                call.respond(HttpStatusCode.OK, statusResponse)
            }
            get("/ids") {
                call.handleSession()
                val mmUser = transaction { MmUser[call.mmSession!!.subject] }
                val opPIDs = call.receive<OpPIDs>()
                val done = transaction { MmSolutionEvent.getSolutionStatuses(mmUser, opPIDs) }
                val running = mutableListOf<MmSolveStatus>()
                runningOpPIDs.forEach {
                    if (it.first.id == mmUser.id && opPIDs.contains(it.second.pid)) running.add(it.second)
                }
                val statusResponse: List<MmSolveStatus> = done + running
                call.respond(HttpStatusCode.OK, statusResponse)
            }
        }
        get("/solution") {
            call.handleSession()
            val mmUser = transaction { MmUser[call.mmSession!!.subject] }
            val opPID = call.receive<OpPID>()
            val solution = transaction { MmSolutionEvent.findSolution(mmUser, opPID) }
            call.respond(HttpStatusCode.OK, solution)
        }
    }
}

/**
 * Checks if a session exists before handling the API calls.
 */
private fun ApplicationCall.handleSession() {
    if (mmSession == null) throw NoSessionException("Missing session!")
}
