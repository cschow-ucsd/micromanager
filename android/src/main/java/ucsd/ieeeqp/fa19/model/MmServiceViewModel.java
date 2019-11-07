package ucsd.ieeeqp.fa19.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import kotlinx.coroutines.future.FutureKt;
import ucsd.ieeeqp.fa19.service.MmService;
import ucsd.ieeeqp.fa19.service.TestResponse;

import java.util.concurrent.CompletableFuture;

public class MmServiceViewModel extends ViewModel {
    private MmService mmService;

    private MutableLiveData<TestResponse> protectedLiveData = new MutableLiveData<>(null);

    public void initService(String serverAuthToken) {
        invalidateService();
        mmService = new MmService(serverAuthToken);
    }

    public void invalidateService() {
        if (mmService != null) {
            mmService.close();
            mmService = null;
        }
    }

    public void fetchProtectedDataAsync() {
        CompletableFuture<String> future = FutureKt.asCompletableFuture(mmService.getProtectedAsync());
        future.handleAsync((result, exception) -> {
            if (exception != null) {
                protectedLiveData.postValue(new TestResponse(false, null));
            } else {
                protectedLiveData.postValue(new TestResponse(true, result));
            }
            return null;
        });
    }

    public LiveData<TestResponse> getProtectedLiveData() {
        return protectedLiveData;
    }

    @Override
    protected void onCleared() {
        invalidateService();
    }
}
