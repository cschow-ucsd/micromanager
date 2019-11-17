package ucsd.ieeeqp.fa19.ui.mm;

public class Event {
    
    private String eventName; 
    private int duration; 
    private double longitude; 
    private double latitude; 
    private int recurrences;
    private int priority;

    public Event(String eventName, int duration, double longitude, double latitude, int reoccurances, int priority) {
        this.eventName = eventName;
        this.duration = duration;
        this.longitude = longitude;
        this.latitude = latitude;
        this.recurrences = recurrences;
        this.priority = priority;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

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
