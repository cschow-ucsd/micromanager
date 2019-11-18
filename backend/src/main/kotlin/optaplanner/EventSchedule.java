package optaplanner;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class EventSchedule {
    private List<PlanningFlexibleEvent> planningFlexibleEventList;
    private HardSoftScore score;

    @PlanningEntityCollectionProperty
    public List<PlanningFlexibleEvent> getPlanningFlexibleEventList() {
        return planningFlexibleEventList;
    }

    @PlanningScore
    public HardSoftScore getScore(){
        return score;
    }
}
