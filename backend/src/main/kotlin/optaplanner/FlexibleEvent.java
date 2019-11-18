package optaplanner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class FlexibleEvent {
    private String name;
    private Integer startTime, endTime, duration;
    private String type;
    public static final String SOME_TYPE = ""; // TODO: kinds constants

    @PlanningVariable(valueRangeProviderRefs = {"availableStartTimes"})
    public Integer getStartTime() {
        return startTime;
    }

    public Integer getDuration() {
        return duration;
    }
}
