package ucsd.ieeeqp.fa19.ui.mm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import optaplanner.BaseFlexibleEvent;
import ucsd.ieeeqp.fa19.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<BaseFlexibleEvent> events = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        TextView duration;
        TextView longitude;
        TextView latitude;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            type = v.findViewById(R.id.type);
            duration = v.findViewById(R.id.duration);
            longitude = v.findViewById(R.id.longitude);
            latitude = v.findViewById(R.id.latitude);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(List<BaseFlexibleEvent> myEvents) {
        events = myEvents;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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