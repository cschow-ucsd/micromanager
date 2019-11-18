package ucsd.ieeeqp.fa19.ui.mm;

import events.BaseFlexibleEvent;
import org.jetbrains.annotations.NotNull;

public class FlexibleEvent implements BaseFlexibleEvent {

    private String name;
    private String type;
    private int duration;
    private double longitude;
    private double latitude;
//    private int recurrences;
//    private int priority;

    public FlexibleEvent(String name, String type, int duration, double longitude, double latitude) {
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
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
    public int getDuration() {
        return duration;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }
}
