package ucsd.ieeeqp.fa19.ui.mm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ucsd.ieeeqp.fa19.R;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    private ArrayList<Event> schedule;

    public ScheduleFragment()
    {
        schedule = new ArrayList<Event>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}

