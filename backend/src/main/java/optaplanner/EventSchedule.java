package optaplanner;

import api.TravelTimeResponse;
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
    private List<BaseFixedEvent> userFixedEventList;
    private List<PlanningFlexibleEvent> planningFlexibleEventList;
    private HardSoftScore score;
    private int currentTime;
    private static final int MINS_PER_DAY = 1400;
    private BaseUserPreferences userPreferences;
    private List<TravelTimeResponse.Result> travelTimes;

    public EventSchedule(
            List<BaseFixedEvent> userFixedEventList,
            List<PlanningFlexibleEvent> planningFlexibleEventList,
            BaseUserPreferences userPreferences,
            int currentTime,
            List<TravelTimeResponse.Result> travelTimes
    ) {
        this.userFixedEventList = userFixedEventList;
        this.planningFlexibleEventList = planningFlexibleEventList;
        this.userPreferences = userPreferences;
        this.currentTime = currentTime;
        this.travelTimes = travelTimes;
    }

    // default constructor for optaplanner
    public EventSchedule() {
        this(new ArrayList<>(), new ArrayList<>(), new BaseUserPreferences(0, 0, 0, 0, 0,
                0, 0, 0, 0, 0), 0, new ArrayList<>());
    }

    @PlanningEntityCollectionProperty
    public List<PlanningFlexibleEvent> getPlanningFlexibleEventList() {
        return planningFlexibleEventList;
    }

    public List<BaseFixedEvent> getUserFixedEventList() {
        return userFixedEventList;
    }

    public void setPlanningFlexibleEventList(List<PlanningFlexibleEvent> planningFlexibleEventList) {
        this.planningFlexibleEventList = planningFlexibleEventList;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public BaseUserPreferences getUserPreferences() {
        return userPreferences;
    }

    public List<TravelTimeResponse.Result> getTravelTimes() {
        return travelTimes;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    @ValueRangeProvider(id = "availableStartTimes")
    @ProblemFactCollectionProperty
    public List<Integer> getAvailableStartTimes() {
        List<Integer> startTimes = new ArrayList<>();
        for (int i = currentTime; i < MINS_PER_DAY; i++) {
            startTimes.add(i);
        }
        return startTimes;
    }
}