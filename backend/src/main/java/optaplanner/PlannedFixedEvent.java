package optaplanner;

public class PlannedFixedEvent extends BaseFixedEvent {

    public PlannedFixedEvent(String name, int startTime, int endTime, double longitude, double latitude) {
        super(name, startTime, endTime, longitude, latitude);
    }
}