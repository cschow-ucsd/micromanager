package ucsd.ieeeqp.fa19.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ucsd.ieeeqp.fa19.ui.new_schedule.FixedEvent;
import ucsd.ieeeqp.fa19.ui.new_schedule.FlexibleEvent;
import ucsd.ieeeqp.fa19.ui.new_schedule.UserPreferences;

import java.util.ArrayList;

public class NewScheduleViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FlexibleEvent>> flexibleEventsLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<ArrayList<FixedEvent>> fixedEventsLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Integer> inputStageLiveData = new MutableLiveData<>(0);
    private UserPreferences userPreferences = new UserPreferences();
    private String scheduleName = "";

    public void addFlexibleEvent(FlexibleEvent event) {
        ArrayList<FlexibleEvent> flexibleEvents = flexibleEventsLiveData.getValue();
        flexibleEvents.add(event);
        flexibleEventsLiveData.setValue(flexibleEvents);
    }

    public void addFixedEvent(FixedEvent event) {
        ArrayList<FixedEvent> fixedEvents = fixedEventsLiveData.getValue();
        fixedEvents.add(event);
        fixedEventsLiveData.setValue(fixedEvents);
    }

    public LiveData<ArrayList<FlexibleEvent>> getFlexibleEventsLiveData() {
        return flexibleEventsLiveData;
    }

    public LiveData<ArrayList<FixedEvent>> getFixedEventsLiveData() {
        return fixedEventsLiveData;
    }

    public LiveData<Integer> getInputStageLiveData() {
        return inputStageLiveData;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public void incrementStage() {
        int current = inputStageLiveData.getValue();
        inputStageLiveData.setValue(current + 1);
    }

    public void decrementStage() {
        int current = inputStageLiveData.getValue();
        if (current > 0) {
            inputStageLiveData.setValue(current - 1);
        }
    }
}
