package optaplanner;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
public class EventSchedule {
    private List<PlannedFixedEvent> userFixedEventList;
    private List<PlanningFlexibleEvent> planningFlexibleEventList;
    private HardSoftScore score;
    private static final int MINS_PER_DAY = 1440;

    public EventSchedule(
            List<PlannedFixedEvent> userFixedEventList,
            List<PlanningFlexibleEvent> planningFlexibleEventList
    ) {
        this.userFixedEventList = userFixedEventList;
        this.planningFlexibleEventList = planningFlexibleEventList;
    }

    @PlanningEntityCollectionProperty
    public List<PlanningFlexibleEvent> getPlanningFlexibleEventList() {
        return planningFlexibleEventList;
    }

    public void setPlanningFlexibleEventList(List<PlanningFlexibleEvent> planningFlexibleEventList) {
        this.planningFlexibleEventList = planningFlexibleEventList;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    @ValueRangeProvider(id = "availableStartTimes")
    @ProblemFactCollectionProperty
    public List<Integer> getAvailableStartTimes() {
        List<Integer> startTimes = new ArrayList<>();
        for (int i = 0; i < MINS_PER_DAY; i++) {
            startTimes.add(i);
        }
        return startTimes;
    }
}