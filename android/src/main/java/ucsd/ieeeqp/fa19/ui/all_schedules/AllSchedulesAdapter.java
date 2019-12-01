package ucsd.ieeeqp.fa19.ui.all_schedules;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import call.MmSolveStatus;
import ucsd.ieeeqp.fa19.R;

import java.util.List;

public class AllSchedulesAdapter extends RecyclerView.Adapter<AllSchedulesAdapter.AllSchedulesViewHolder> {
    private List<MmSolveStatus> statuses;
    private View.OnClickListener listener;

    public AllSchedulesAdapter(List<MmSolveStatus> statuses, View.OnClickListener listener) {
        this.statuses = statuses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AllSchedulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.item_schedule, parent, false);
        rootView.setOnClickListener(listener);
        return new AllSchedulesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllSchedulesViewHolder holder, int position) {
        MmSolveStatus item = statuses.get(position);
        holder.nameTextView.setText(item.getScheduleName());
        holder.idTextView.setText(item.getPid());
        holder.statusTextView.setText(item.getDone() ? R.string.status_done : R.string.status_running);
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    static class AllSchedulesViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView idTextView;
        TextView statusTextView;

        public AllSchedulesViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_item_schedule_name);
            idTextView = itemView.findViewById(R.id.textview_item_schedule_id);
            statusTextView = itemView.findViewById(R.id.textview_item_schedule_status);
        }
    }
}
