import com.typesafe.config.ConfigException
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import optaplanner.EventSchedule
import org.optaplanner.core.api.solver.SolverFactory

/**
 * Entry point that starts the server.
 * Uses [EngineMain] with application.conf to start Netty server.
 */
@KtorExperimentalAPI
fun main(args: Array<String>) {
    solverTest()
//    try {
//        EngineMain.main(args)
//    } catch (e: ConfigException.UnresolvedSubstitution) {
//        throw RuntimeException("Unresolved substitution in config. Missing reference.conf in resources?", e)
//    }
}

private fun solverTest() {
    val solverFactory = SolverFactory
            .createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")
    val solver = solverFactory.buildSolver()

    val unsolvedEventSchedule = EventSchedule()

    val solvedEventSchedule = solver.solve(unsolvedEventSchedule) as EventSchedule
}
