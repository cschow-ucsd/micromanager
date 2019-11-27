package ucsd.ieeeqp.fa19.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import call.MmSolveStatus;
import kotlinx.coroutines.future.FutureKt;
import ucsd.ieeeqp.fa19.service.MmService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class MmServiceViewModel extends AndroidViewModel {
    public static final int NOT_LOGGED_IN = 1000, LOGIN_FAILED = 1001, LOGIN_SUCCESS = 1002;
    private static final long QUERY_PERIOD = 5000;

    private MmService mmService;
    private MutableLiveData<Integer> mmLoginStateLiveData = new MutableLiveData<>(NOT_LOGGED_IN);
    private MutableLiveData<List<MmSolveStatus>> mmSolveStatusLiveData = new MutableLiveData<>(null);
    private Timer queryTimer = new Timer();

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

    public void startQueryAllStatuses() {
        queryTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                queryAllStatuses();
            }
        }, 0, QUERY_PERIOD);
    }

    public void queryAllStatuses() {
        CompletableFuture<List<MmSolveStatus>> future = FutureKt.asCompletableFuture(mmService.getAllProgressAsync());
        future.handleAsync((result, exception) -> {
            if (exception != null) {
                mmSolveStatusLiveData.postValue(result);
            }
            return null;
        });
    }

    public LiveData<Integer> getMmLoginStateLiveData() {
        return mmLoginStateLiveData;
    }

    public LiveData<List<MmSolveStatus>> getMmSolveStatusLiveData() {
        return mmSolveStatusLiveData;
    }

    @Override
    protected void onCleared() {
        invalidateService();
        queryTimer.cancel();
    }
}
