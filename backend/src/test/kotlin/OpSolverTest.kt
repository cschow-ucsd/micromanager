import kotlinx.coroutines.*
import optaplanner.*
import org.junit.Test
import org.optaplanner.core.api.solver.SolverFactory

class OpSolverTest {
    @Test
    fun emptySolver() {
        val solverFactory: SolverFactory<EventSchedule> = SolverFactory
                .createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")
        val solver = solverFactory.buildSolver()
        val unsolved = EventSchedule(emptyList(), emptyList(), BaseUserPreferences(0, 0, 0, 0, 0, 0, 0, 0, 0, 0), 0)
        val solved = solver.solve(unsolved)
    }

    @Test
    fun solveSchedule() = runBlocking<Unit> {
        val s = SolverFactory.createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")
        val test = s.buildSolver()
        launch {
            delay(1000)
            test.terminateEarly()
        }
        val a = arrayListOf<BaseFixedEvent>(
                PlannedFixedEvent("Sleep", 0, 420, 0.0, 0.0),
                PlannedFixedEvent("IEEE Workathon", 840, 960, 0.0, 0.0),
                PlannedFixedEvent("CSE 20", 540, 600, 0.0, 0.0)
        )
        val b = arrayListOf(
                PlanningFlexibleEvent("Breakfast", "Breakfast", 40, 0.0, 0.0),
                PlanningFlexibleEvent("Lunch", "Lunch", 70, 0.0, 0.0),
                PlanningFlexibleEvent("Dinner", "Dinner", 60, 0.0, 0.0),
                PlanningFlexibleEvent("CSE 20 HW", "HW", 50, 0.0, 0.0),
                PlanningFlexibleEvent("Work on QP Project", "Club", 120, 0.0, 0.0)
        )
        val e = EventSchedule(a, b,
                BaseUserPreferences(460, 480, 600, 630, 960, 1000, 0, 0, 60, 60), 0)
        val x = withContext(Dispatchers.Default) { test.solve(e) }
        for (pr in x.planningFlexibleEventList) {
            println(pr.name + " of type " + pr.type + " starts at: " + pr.startTime)
        }
    }
}