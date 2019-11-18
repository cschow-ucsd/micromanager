package optaplanner;

import model.BaseFlexibleEvent;
import org.jetbrains.annotations.NotNull;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class PlanningFlexibleEvent implements BaseFlexibleEvent {
    private String name;
    private Integer startTime, endTime, duration;
    private String type;
    private double longitude, latitude;
    public static final String SOME_TYPE = ""; // TODO: kinds constants

    @PlanningVariable(valueRangeProviderRefs = {"availableStartTimes"})
    public Integer getStartTime() {
        return startTime;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @NotNull
    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(@NotNull String type) {
        this.type = type;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
