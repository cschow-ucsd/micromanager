package ucsd.ieeeqp.fa19.ui.mm;

import model.BaseFlexibleEvent;
import org.jetbrains.annotations.NotNull;

public class FlexibleEvent implements BaseFlexibleEvent {

    private String eventName;
    private String type;
    private int duration;
    private double longitude;
    private double latitude;
    private int recurrences;
    private int priority;

    public FlexibleEvent(
            String eventName,
            String type,
            int duration,
            double longitude,
            double latitude,
            int recurrences,
            int priority
    ) {
        this.eventName = eventName;
        this.type = type;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
        this.recurrences = recurrences;
        this.priority = priority;
    }

    @NotNull
    @Override
    public String getName() {
        return eventName;
    }

    @Override
    public void setName(@NotNull String eventName) {
        this.eventName = eventName;
    }

    @NotNull
    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(@NotNull String type) {
        this.type = type;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getRecurrences() {
        return recurrences;
    }

    public void setRecurrences(int recurrences) {
        this.recurrences = recurrences;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
