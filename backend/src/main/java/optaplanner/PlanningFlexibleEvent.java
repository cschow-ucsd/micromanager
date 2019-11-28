package optaplanner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class PlanningFlexibleEvent extends BaseFlexibleEvent {
    private Integer startTime;

    public PlanningFlexibleEvent(String name, String type, int duration, double longitude, double latitude) {
        super(name, type, duration, longitude, latitude);
    }

    public PlanningFlexibleEvent() {
        this("", "", 0, 0.0, 0);
    }

    @PlanningVariable(valueRangeProviderRefs = "availableStartTimes", nullable = false)
    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }
}