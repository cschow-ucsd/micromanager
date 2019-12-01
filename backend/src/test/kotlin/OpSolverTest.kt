import optaplanner.BaseUserPreferences
import optaplanner.EventSchedule
import org.junit.Test
import org.optaplanner.core.api.solver.SolverFactory

class OpSolverTest {
    @Test
    fun solver() {
        val solverFactory: SolverFactory<EventSchedule> = SolverFactory
                .createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")
        val solver = solverFactory.buildSolver()
        val unsolved = EventSchedule(emptyList(), emptyList(), BaseUserPreferences(0, 0, 0, 0, 0, 0, 0, 0, 0 ,0),0)
        val solved = solver.solve(unsolved)
    }
}   