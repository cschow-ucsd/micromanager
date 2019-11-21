import optaplanner.EventSchedule
import org.junit.Test
import org.optaplanner.core.api.solver.SolverFactory

class OpSolverTest {
    @Test
    fun solver() {
        val solverFactory: SolverFactory<EventSchedule> = SolverFactory
                .createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")
        val solver = solverFactory.buildSolver()
        val unsolvedEventSchedule = EventSchedule(emptyList(), emptyList())
        val solvedEventSchedule = solver.solve(unsolvedEventSchedule)
    }
}