package optaplanner;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.domain.valuerange.buildin.primint.IntValueRange;

import java.util.List;

@PlanningSolution
public class EventSchedule {
    private List<PlanningFlexibleEvent> planningFlexibleEventList;
    private HardSoftScore score;
    private static final int MINS_PER_DAY = 1440;

    @PlanningEntityCollectionProperty
    public List<PlanningFlexibleEvent> getPlanningFlexibleEventList() {
        return planningFlexibleEventList;
    }

    @PlanningScore
    public HardSoftScore getScore(){
        return score;
    }

    @ValueRangeProvider(id = "availableStartTimes")
    @ProblemFactCollectionProperty
    public IntValueRange getAvailableStartTimes(){
        return new IntValueRange(0, MINS_PER_DAY);
    }
}
