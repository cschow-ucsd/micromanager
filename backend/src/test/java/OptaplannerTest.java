import optaplanner.*;
import org.junit.Before;
import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.util.ArrayList;

public class OptaplannerTest {
    @Before
    public void before() {

    }

    @Test
    public void optaplannerTest() {
        ArrayList<BaseFixedEvent> a = new ArrayList<>();
        a.add(new PlannedFixedEvent("Sleep", 0, 0, 0, 420));
        a.add(new PlannedFixedEvent("IEEE Workathon", 0, 0, 840, 960));
        a.add(new PlannedFixedEvent("CSE 20", 0, 0, 540, 590));
        ArrayList<PlanningFlexibleEvent> b = new ArrayList<>();
        b.add(new PlanningFlexibleEvent("Breakfast", "Breakfast", 0, 0, 20));
        b.add(new PlanningFlexibleEvent("Lunch", "Lunch", 0, 0, 30));
        b.add(new PlanningFlexibleEvent("Dinner", "Dinner", 0 ,0, 30));
        b.add(new PlanningFlexibleEvent("CSE 20 HW", "HW", 0, 0, 180));
        b.add(new PlanningFlexibleEvent("Work on QP Project", "Club", 0, 0, 180));
        EventSchedule e = new EventSchedule(a, b,
                new BaseUserPreferences(460, 480, 600, 630, 960, 1000, 60, 60));
        SolverFactory<EventSchedule> s = SolverFactory.createFromXmlResource("event_schedule_solver_configuration.xml");
        Solver<EventSchedule> test = s.buildSolver();
        EventSchedule x = test.solve(e);
    }
}