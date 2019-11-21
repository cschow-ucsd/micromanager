package ucsd.ieeeqp.fa19.ui.mm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import op.BaseFlexibleEvent;
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

        // TODO: use view.findViewById() to get the floating action button (refer to MainActivity if you forgot how to do that)
        // TODO: Set a listener on the FAB and launch NewEventFragment when clicked
        // TODO: Set a NewEventListener on the NewEventFragment to get the event from the fragment and add it to events after the event is submitted)
    }
}

