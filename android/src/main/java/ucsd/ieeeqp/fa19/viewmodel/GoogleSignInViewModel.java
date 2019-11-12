package ucsd.ieeeqp.fa19.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import ucsd.ieeeqp.fa19.R;

public class GoogleSignInViewModel extends AndroidViewModel {

    private static final String TAG = GoogleSignInViewModel.class.getSimpleName();
    public static final int ACCOUNT_FINDING = 1000, ACCOUNT_NOT_FOUND = 1001, ACCOUNT_FOUND = 1002, ACCOUNT_LOGIN_SUCCESS = 1003, ACCOUNT_LOGIN_FAILED = 1004;

    private MutableLiveData<Integer> googleLoginStateLiveData = new MutableLiveData<>(ACCOUNT_FINDING);
    private GoogleSignInAccount account;

    private final GoogleSignInClient client;

    public GoogleSignInViewModel(@NonNull Application application) {
        super(application);

        // sign in configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(application.getString(R.string.oauth_client_id))
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();
        client = GoogleSignIn.getClient(application, gso);
    }

    public void findExistingAccount() {
        GoogleSignInAccount existingAccount = GoogleSignIn.getLastSignedInAccount(getApplication());
        if (existingAccount != null) {
            account = existingAccount;
            googleLoginStateLiveData.postValue(ACCOUNT_FOUND);
        } else {
            googleLoginStateLiveData.postValue(ACCOUNT_NOT_FOUND);
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount newSignInAccount = completedTask.getResult(ApiException.class);
            if (newSignInAccount != null) {
                account = newSignInAccount;
                googleLoginStateLiveData.postValue(ACCOUNT_LOGIN_SUCCESS);
                Log.d(TAG, "handleSignInResult: Sign in successful. Server auth token: " + newSignInAccount.getServerAuthCode());
            } else {
                googleLoginStateLiveData.postValue(ACCOUNT_LOGIN_FAILED);
            }
        } catch (ApiException e) {
            googleLoginStateLiveData.postValue(ACCOUNT_LOGIN_FAILED);
            Log.d(TAG, "handleSignInResult: Sign in failed.");
        }
    }

    public LiveData<Integer> getGoogleLoginStateLiveData() {
        return googleLoginStateLiveData;
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public GoogleSignInClient getClient() {
        return client;
    }
}