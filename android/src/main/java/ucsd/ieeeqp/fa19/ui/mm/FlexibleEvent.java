package ucsd.ieeeqp.fa19.ui.mm;

import android.os.Parcel;
import android.os.Parcelable;
import optaplanner.BaseFlexibleEvent;
import org.jetbrains.annotations.NotNull;

public class FlexibleEvent implements BaseFlexibleEvent, Parcelable {

    private String name;
    private String type;
    private int duration;
    private double longitude;
    private double latitude;

    // Alan still has to get these to work
    // private int recurrences;
    // private int priority;

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

    protected FlexibleEvent(Parcel in) {
        name = in.readString();
        type = in.readString();
        duration = in.readInt();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeInt(duration);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FlexibleEvent> CREATOR = new Parcelable.Creator<FlexibleEvent>() {
        @Override
        public FlexibleEvent createFromParcel(Parcel in) {
            return new FlexibleEvent(in);
        }

        @Override
        public FlexibleEvent[] newArray(int size) {
            return new FlexibleEvent[size];
        }
    };
}
