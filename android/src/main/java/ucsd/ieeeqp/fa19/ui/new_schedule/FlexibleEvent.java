package ucsd.ieeeqp.fa19.ui.new_schedule;

import android.os.Parcel;
import android.os.Parcelable;
import optaplanner.BaseFlexibleEvent;

public class FlexibleEvent extends BaseFlexibleEvent implements Parcelable {
    // Alan still has to get these to work
    // private int recurrences;
    // private int priority;

    public FlexibleEvent(String name, String type, int duration, double longitude, double latitude) {
        super(name, type, duration, longitude, latitude);
    }

    private FlexibleEvent(Parcel in) {
        super(in.readString(), in.readString(), in.readInt(), in.readDouble(), in.readDouble());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getType());
        dest.writeInt(getDuration());
        dest.writeDouble(getLongitude());
        dest.writeDouble(getLatitude());
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
