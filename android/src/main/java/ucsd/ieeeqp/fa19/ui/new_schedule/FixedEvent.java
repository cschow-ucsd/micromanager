package ucsd.ieeeqp.fa19.ui.new_schedule;

import android.os.Parcel;
import android.os.Parcelable;
import optaplanner.BaseFixedEvent;
import org.jetbrains.annotations.NotNull;

public class FixedEvent implements BaseFixedEvent, Parcelable {
    private String name;
    private int startTime, endTime;
    private double longitude, latitude;

    public FixedEvent(String name, int startTime, int endTime, double longitude, double latitude) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    protected FixedEvent(Parcel in) {
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FixedEvent> CREATOR = new Parcelable.Creator<FixedEvent>() {
        @Override
        public FixedEvent createFromParcel(Parcel in) {
            return new FixedEvent(in);
        }

        @Override
        public FixedEvent[] newArray(int size) {
            return new FixedEvent[size];
        }
    };
}