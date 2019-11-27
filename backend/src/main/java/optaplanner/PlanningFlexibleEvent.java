package optaplanner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class PlanningFlexibleEvent extends BaseFlexibleEvent {
    private Integer startTime;

    public PlanningFlexibleEvent(String name, String type, double longitude, double latitude, int duration) {
        super(name, longitude, latitude, type, duration);
    }

    public PlanningFlexibleEvent() {
        this("", "", 0.0, 0.0, 0);
    }

    @PlanningVariable(valueRangeProviderRefs = "availableStartTimes")
    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }
}