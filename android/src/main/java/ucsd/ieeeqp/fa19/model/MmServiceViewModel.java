package ucsd.ieeeqp.fa19.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ucsd.ieeeqp.fa19.service.MmResponseCallback;
import ucsd.ieeeqp.fa19.service.MmService;
import ucsd.ieeeqp.fa19.service.TestResponse;

public class MmServiceViewModel extends ViewModel {
    private MmService mmService;

    private MutableLiveData<TestResponse> protectedData = new MutableLiveData<>(null);

    public void initService(String idToken) {
        invalidateService();
        mmService = new MmService(idToken);
    }

    public void invalidateService() {
        if (mmService != null) {
            mmService.close();
            mmService = null;
        }
    }

    public void fetchProtectedDataAsync() {
        mmService.getProtectedAsync(new MmResponseCallback<String>() {
            @Override
            public void handleResponse(String response) {
                protectedData.postValue(new TestResponse(true, response));
            }

            @Override
            public void handleError(Throwable t) {
                protectedData.postValue(new TestResponse(false, null));
            }
        });
    }

    public LiveData<TestResponse> protectedDataLiveData() {
        return protectedData;
    }

    @Override
    protected void onCleared() {
        invalidateService();
    }
}
