package ucsd.ieeeqp.fa19.ui.mm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import optaplanner.BaseFlexibleEvent;
import ucsd.ieeeqp.fa19.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {
    private List<BaseFlexibleEvent> flexibleEvents = new ArrayList<>();

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
        FloatingActionButton newEventButton = view.findViewById(R.id.fab_schedule_newevent);
        // TODO: Set a listener on the FAB and launch NewEventFragment when clicked
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
        // TODO: Set a NewEventListener on the NewEventFragment to get the event from the fragment and add it to events after the event is submitted)
    }

    private NewEventFragment setupNewEventFragment() {
        NewEventFragment fragment = new NewEventFragment();
        fragment.setNewEventListener(new NewEventFragment.NewEventListener() {
            @Override
            public void onEventSubmitted(BaseFlexibleEvent event) {
                flexibleEvents.add(event);
                RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(flexibleEvents);
                mAdapter.notifyDataSetChanged();
            }
        });
        return fragment;
    }
}

