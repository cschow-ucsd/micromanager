package ucsd.ieeeqp.fa19.ui.mm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import call.MmSolveStatus;
import ucsd.ieeeqp.fa19.R;

import java.util.List;

public class AllSchedulesAdapter extends RecyclerView.Adapter<AllSchedulesAdapter.AllEventsViewHolder> {
    private List<MmSolveStatus> statuses;

    public AllSchedulesAdapter(List<MmSolveStatus> statuses) {
        this.statuses = statuses;
    }

    @NonNull
    @Override
    public AllEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.item_schedule, parent, false);
        return new AllEventsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllEventsViewHolder holder, int position) {
        MmSolveStatus item = statuses.get(position);
        holder.idTextView.setText(item.getPid());
        if (item.getDone()) {
            holder.statusTextView.setText(R.string.status_done);
        } else {
            holder.statusTextView.setText(R.string.status_running);
        }
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    static class AllEventsViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView statusTextView;

        public AllEventsViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.textview_item_schedule_id);
            statusTextView = itemView.findViewById(R.id.textview_item_schedule_status);
        }
    }
}
