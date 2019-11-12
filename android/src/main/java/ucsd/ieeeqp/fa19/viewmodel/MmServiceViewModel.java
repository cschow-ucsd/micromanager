package ucsd.ieeeqp.fa19.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import kotlinx.coroutines.future.FutureKt;
import ucsd.ieeeqp.fa19.service.MmService;

import java.util.concurrent.CompletableFuture;

public class MmServiceViewModel extends AndroidViewModel {
    public static final int NOT_LOGGED_IN = 1000, LOGGING_IN = 1001, LOGIN_FAILED = 1002, LOGIN_SUCCESS = 1003;

    private MmService mmService;
    private MutableLiveData<Integer> mmLoginStateLiveData = new MutableLiveData<>(NOT_LOGGED_IN);

    public MmServiceViewModel(@NonNull Application application) {
        super(application);
    }

    public void initService(String serverAuthCode) {
        invalidateService();
        mmService = new MmService(getApplication(), serverAuthCode);
    }

    public void invalidateService() {
        if (mmService != null) {
            mmService.close();
            mmService = null;
        }
    }

    public void loginToApi() {
        mmLoginStateLiveData.postValue(LOGGING_IN);
        CompletableFuture<Boolean> loginFuture = FutureKt.asCompletableFuture(mmService.loginAsync());
        loginFuture.handleAsync((result, exception) -> {
            if (exception != null) {
                mmLoginStateLiveData.postValue(LOGIN_FAILED);
            } else if (result) {
                mmLoginStateLiveData.postValue(LOGIN_SUCCESS);
            } else {
                mmLoginStateLiveData.postValue(LOGIN_FAILED);
            }
            return null;
        });
    }

    public LiveData<Integer> getMmLoginStateLiveData() {
        return mmLoginStateLiveData;
    }

    @Override
    protected void onCleared() {
        invalidateService();
    }
}
