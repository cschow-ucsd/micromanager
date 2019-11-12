package ucsd.ieeeqp.fa19.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import kotlin.Unit;
import kotlinx.coroutines.future.FutureKt;
import ucsd.ieeeqp.fa19.service.MmHttpClient;
import ucsd.ieeeqp.fa19.service.MmService;

import java.util.concurrent.CompletableFuture;

public class MmServiceViewModel extends AndroidViewModel {
    public static final int NOT_LOGGED_IN = 1000, LOGIN_FAILED = 1001, LOGIN_SUCCESS = 1002;

    private MmService mmService;
    private MutableLiveData<Integer> mmLoginStateLiveData = new MutableLiveData<>(NOT_LOGGED_IN);

    // store previous sessions with the server
    private SharedPreferences preferences;

    public MmServiceViewModel(@NonNull Application application) {
        super(application);
        preferences = application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
    }

    public void initService(String serverAuthCode) {
        invalidateService();
        String lastSessionAuthHeader = preferences.getString(MmHttpClient.MICROMANAGER_SESSION, null);
        mmService = new MmService(serverAuthCode, lastSessionAuthHeader, header -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(MmHttpClient.MICROMANAGER_SESSION, header);
            editor.apply();
            return Unit.INSTANCE;
        });
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

    public LiveData<Integer> getMmLoginStateLiveData() {
        return mmLoginStateLiveData;
    }

    @Override
    protected void onCleared() {
        invalidateService();
    }
}
