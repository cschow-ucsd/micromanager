package ucsd.ieeeqp.fa19.ui.new_schedule;

import android.os.Parcel;
import android.os.Parcelable;
import optaplanner.BaseFixedEvent;

public class FixedEvent extends BaseFixedEvent implements Parcelable {

    public FixedEvent(String name, int startTime, int endTime, double longitude, double latitude) {
        super(name, longitude, latitude, startTime, endTime);
    }

    private FixedEvent(Parcel in) {
        super(in.readString(), in.readInt(), in.readDouble(), in.readDouble());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeInt(getStartTime());
        dest.writeInt(getEndTime());
        dest.writeDouble(getLongitude());
        dest.writeDouble(getLatitude());
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