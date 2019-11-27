package ucsd.ieeeqp.fa19.ui.new_schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import optaplanner.BaseFlexibleEvent;
import org.jetbrains.annotations.NotNull;
import ucsd.ieeeqp.fa19.R;

import java.util.List;

public class NewScheduleAdapter extends RecyclerView.Adapter<NewScheduleAdapter.NewScheduleViewHolder> {
    private List<BaseFlexibleEvent> events;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class NewScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        TextView duration;
        TextView longitude;
        TextView latitude;

        public NewScheduleViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.textview_flexibleitem_name);
            type = v.findViewById(R.id.textview_flexibleitem_starttime);
            duration = v.findViewById(R.id.textview_flexibleitem_duration);
            longitude = v.findViewById(R.id.textview_flexibleitem_longitude);
            latitude = v.findViewById(R.id.textview_flexibleitem_latitude);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewScheduleAdapter(List<BaseFlexibleEvent> myEvents) {
        events = myEvents;
    }

    @NotNull
    @Override
    public NewScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.flexibleitem_event, parent, false);
        return new NewScheduleViewHolder(rootView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NewScheduleViewHolder holder, int position) {
        holder.name.setText(events.get(position).getName());
        holder.type.setText(events.get(position).getType());
        holder.duration.setText(events.get(position).getDuration());
        holder.longitude.setText("" + events.get(position).getLongitude());
        holder.latitude.setText("" + events.get(position).getLatitude());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }
}