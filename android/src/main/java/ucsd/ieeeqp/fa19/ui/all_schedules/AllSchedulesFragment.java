package ucsd.ieeeqp.fa19.ui.all_schedules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import call.MmProblemRequest;
import call.MmSolveStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import optaplanner.BaseUserPreferences;
import ucsd.ieeeqp.fa19.R;
import ucsd.ieeeqp.fa19.ui.new_schedule.FixedEvent;
import ucsd.ieeeqp.fa19.ui.new_schedule.FlexibleEvent;
import ucsd.ieeeqp.fa19.ui.new_schedule.NewScheduleActivity;
import ucsd.ieeeqp.fa19.viewmodel.MmServiceViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllSchedulesFragment extends Fragment {
    private static final int RC_NEW_SCHEDULE = 7984;
    private List<MmSolveStatus> statuses = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private MmServiceViewModel mmServiceViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_schedules, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView allSchedulesRecyclerView = view.findViewById(R.id.recyclerview_allschedules_allschedules);
        setupRecyclerView(allSchedulesRecyclerView);
        FloatingActionButton fab = view.findViewById(R.id.fab_allschedules_newschedule);
        fab.setOnClickListener(v -> {
            Intent newScheduleIntent = new Intent(getContext(), NewScheduleActivity.class);
            startActivityForResult(newScheduleIntent, RC_NEW_SCHEDULE);
        });
        setupQueryProblemStatuses();
    }

    private void setupQueryProblemStatuses() {
        mmServiceViewModel = ViewModelProviders.of(getActivity()).get(MmServiceViewModel.class);
        mmServiceViewModel.getMmSolveStatusLiveData().observe(this, mmSolveStatuses -> {
            if (mmSolveStatuses != null) {
                statuses.clear();
                statuses.addAll(mmSolveStatuses);
                adapter.notifyDataSetChanged();
            }
        });
        mmServiceViewModel.startQueryAllStatuses();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AllSchedulesAdapter(statuses);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_NEW_SCHEDULE && resultCode == Activity.RESULT_OK && data != null) {
            List<FixedEvent> userFixedEvents = data.getParcelableArrayListExtra(NewScheduleActivity.FIXED_EXTRA);
            List<FlexibleEvent> toPlanEvents = data.getParcelableArrayListExtra(NewScheduleActivity.FLEXIBLE_EXTRA);
            mmServiceViewModel.submitUnsolvedSchedule(new MmProblemRequest(userFixedEvents, toPlanEvents,
                    new BaseUserPreferences(0, 0, 0, 0, 0, 0, 0, 0)));
        }
    }
}
