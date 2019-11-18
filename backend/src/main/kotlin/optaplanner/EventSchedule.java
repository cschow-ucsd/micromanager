package optaplanner;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class EventSchedule {
    private List<FlexibleEvent> flexibleEventList;
    private HardSoftScore score;

    @PlanningEntityCollectionProperty
    public List<FlexibleEvent> getFlexibleEventList() {
        return flexibleEventList;
    }

    @PlanningScore
    public HardSoftScore getScore(){
        return score;
    }
}
