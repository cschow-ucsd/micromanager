package ucsd.ieeeqp.fa19.ui.new_schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import optaplanner.BaseFlexibleEvent;
import ucsd.ieeeqp.fa19.R;

public class NewEventFragment extends Fragment {
    private NewEventListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: obtain all the EditText on the screen with view.findViewById()
        // TODO: When the submit button is clicked, create a new event with all info entered in the EditText and call submit()
    }

    /**
     * Called when a new event is created and submitted.
     * Invokes a callback on the NewEventListener.
     *
     * @param event new event created
     */
    private void submit(BaseFlexibleEvent event) {
        if (listener != null) {
            listener.onEventSubmitted(event);
        }
        getFragmentManager().popBackStack();
    }

    public void setNewEventListener(NewEventListener listener) {
        this.listener = listener;
    }

    /**
     * Custom listener to listen for the creation of new events.
     */
    public interface NewEventListener {
        void onEventSubmitted(BaseFlexibleEvent event);
    }
}

