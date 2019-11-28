package ucsd.ieeeqp.fa19.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import call.MmProblemRequest;
import call.MmSolveStatus;
import kotlinx.coroutines.future.FutureKt;
import ucsd.ieeeqp.fa19.service.MmService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MmServiceViewModel extends AndroidViewModel {
    public static final int NOT_LOGGED_IN = 1000, LOGIN_FAILED = 1001, LOGIN_SUCCESS = 1002;
    private static final long QUERY_PERIOD = 5000;

    private MmService mmService;
    private MutableLiveData<Integer> mmLoginStateLiveData = new MutableLiveData<>(NOT_LOGGED_IN);
    private MutableLiveData<List<MmSolveStatus>> mmSolveStatusLiveData = new MutableLiveData<>(new ArrayList<>());
    private Timer queryTimer;

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
        if (queryTimer != null) {
            queryTimer.cancel();
        }
        queryTimer = new Timer();
        queryTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                queryAllStatuses();
            }
        }, 0, QUERY_PERIOD);
    }

    private void queryAllStatuses() {
        CompletableFuture<List<MmSolveStatus>> future = FutureKt.asCompletableFuture(mmService.getAllProgressAsync());
        future.handleAsync((result, exception) -> {
            if (exception != null) {
                Log.d(TAG, "queryAllStatuses: result: " + result);
                mmSolveStatusLiveData.postValue(result);
            }
            return null;
        });
    }

    public void submitUnsolvedSchedule(MmProblemRequest request) {
        CompletableFuture<MmSolveStatus> future = FutureKt.asCompletableFuture(mmService.solveProblemAsync(request));
        future.handleAsync((result, exception) -> {
            String message = (exception == null) ? "Solve accepted; ID: " + result.getPid() : exception.getMessage();
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
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
        if (queryTimer != null) {
            queryTimer.cancel();
        }
    }
}
