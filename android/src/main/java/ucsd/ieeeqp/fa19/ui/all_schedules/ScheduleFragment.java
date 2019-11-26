package ucsd.ieeeqp.fa19.ui.all_schedules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import optaplanner.BaseFlexibleEvent;
import ucsd.ieeeqp.fa19.R;
import ucsd.ieeeqp.fa19.ui.new_schedule.NewEventFragment;
import ucsd.ieeeqp.fa19.ui.new_schedule.NewScheduleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {
    private List<BaseFlexibleEvent> flexibleEvents = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: display events on the screen with RecyclerView


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        setupRecyclerView(recyclerView);
        FloatingActionButton newEventButton = view.findViewById(R.id.fab_schedule_newevent);

        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEventFragment fragment = setupNewEventFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.viewpager_nav_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewScheduleAdapter(flexibleEvents);
        recyclerView.setAdapter(adapter);
    }

    private NewEventFragment setupNewEventFragment() {
        NewEventFragment fragment = new NewEventFragment();
        fragment.setNewEventListener(new NewEventFragment.NewEventListener() {
            @Override
            public void onEventSubmitted(BaseFlexibleEvent event) {
                flexibleEvents.add(event);
                adapter.notifyDataSetChanged();
            }
        });
        return fragment;
    }
}

