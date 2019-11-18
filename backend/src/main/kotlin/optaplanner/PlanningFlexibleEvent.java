package optaplanner;

import org.jetbrains.annotations.NotNull;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class PlanningFlexibleEvent implements BaseFlexibleEvent {
    // Planning variable
    private int startTime;

    // Event variables
    private String name, type;
    private double longitude, latitude;
    private int duration;

    public PlanningFlexibleEvent(String name, String type, double longitude, double latitude, int duration) {
        this.name = name;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.duration = duration;
    }

    public PlanningFlexibleEvent(@NotNull BaseFlexibleEvent e) {
        this(e.getName(), e.getType(), e.getLongitude(), e.getLatitude(), e.getDuration());
    }

    public PlannedFixedEvent toPlannedFixedEvent() {
        return new PlannedFixedEvent(this);
    }

    @PlanningVariable(valueRangeProviderRefs = {"availableStartTimes"})
    public Integer getStartTime() {
        return startTime;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getType() {
        return type;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
