package api

import call.*
import exposed.dao.MmSolutionSchedule
import exposed.dao.MmUser
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
import optaplanner.EventSchedule
import optaplanner.OpPID
import optaplanner.OpPIDs
import org.jetbrains.exposed.sql.transactions.transaction
import org.optaplanner.core.api.solver.SolverFactory
import util.*

private val runningProcesses: MutableList<Pair<MmUser, MmSolveStatus>> = mutableListOf()
private val opSolverFactory: SolverFactory<EventSchedule> = SolverFactory
        .createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")

/**
 * Public routes.
 * Can be accessed by directly calling the endpoints.
 */
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
            call.respond(HttpStatusCode.OK)
        }
        post("/solve") {
            val mmUser = transaction { MmUser[call.mmSession!!.subject] }
            val problem = call.receive<MmProblemRequest>()
            val mmSolveStatus = problem.solve(opSolverFactory.buildSolver(), mmUser) {
                call.logger.debug("Created")
                runningProcesses += mmUser to it
                call.respond(HttpStatusCode.Accepted, it)
                call.logger.debug("Created")
            }
            runningProcesses.removeIf { it.second.pid == mmSolveStatus.pid }
        }
        route("/status") {
            get("/all") {
                call.logger.debug("Status query received.")
                val mmUser = transaction { MmUser[call.mmSession!!.subject] }
                val userOpPIDs: OpPIDs = transaction { mmUser.schedules.map { OpPID(it.id.value) } }
                val done = MmSolutionSchedule.getSolutionStatuses(mmUser, userOpPIDs)
                val running = mutableListOf<MmSolveStatus>()
                runningProcesses.forEach {
                    if (it.first.id == mmUser.id) running.add(it.second)
                }
                val statusResponse: MmStatusResponse = done + running
                call.logger.debug("Status response created: $statusResponse")
                call.respond(HttpStatusCode.OK, statusResponse)
            }
            post("/ids") {
                val mmUser = transaction { MmUser[call.mmSession!!.subject] }
                val opPIDs = call.receive<OpPIDs>()
                val done = MmSolutionSchedule.getSolutionStatuses(mmUser, opPIDs)
                val running = mutableListOf<MmSolveStatus>()
                runningProcesses.forEach {
                    if (it.first.id == mmUser.id && opPIDs.contains(OpPID(it.second.pid))) running.add(it.second)
                }
                val statusResponse: List<MmSolveStatus> = done + running
                call.respond(HttpStatusCode.OK, statusResponse)
            }
        }
        post("/solution") {
            val mmUser = transaction { MmUser[call.mmSession!!.subject] }
            val opPID = call.receive<OpPID>()
            val solution = transaction { MmSolutionSchedule.findSolution(mmUser, opPID) }
            call.respond(HttpStatusCode.OK, solution)
        }
    }
}
