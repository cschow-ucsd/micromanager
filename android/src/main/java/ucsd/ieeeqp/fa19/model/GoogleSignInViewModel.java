package ucsd.ieeeqp.fa19.model;

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

    private MutableLiveData<Boolean> accountIsValidLiveData = new MutableLiveData<>(false);
    private MutableLiveData<GoogleSignInAccount> accountLiveData = new MutableLiveData<>(null);

    private final GoogleSignInOptions gso;
    private final GoogleSignInClient client;

    public GoogleSignInViewModel(@NonNull Application application) {
        super(application);

        // sign in configuration
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getString(R.string.oauth_client_id))
                .requestServerAuthCode(application.getString(R.string.oauth_client_id))
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();
        client = GoogleSignIn.getClient(application, gso);
    }

    public void findExistingAccount() {
        GoogleSignInAccount existingAccount = GoogleSignIn.getLastSignedInAccount(getApplication());
        if (existingAccount != null) {
            accountLiveData.setValue(existingAccount);
            accountIsValidLiveData.setValue(true);
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount newSignInAccount = completedTask.getResult(ApiException.class);
            accountLiveData.setValue(newSignInAccount);
            accountIsValidLiveData.setValue(true);
            Log.d(TAG, "handleSignInResult: Sign in successful. Server auth token: " + newSignInAccount.getServerAuthCode());
            Log.d(TAG, "handleSignInResult: Sign in successful. Server auth token: " + newSignInAccount.getIdToken());
        } catch (ApiException e) {
            accountIsValidLiveData.setValue(false);
            accountLiveData.setValue(null);
            Log.d(TAG, "handleSignInResult: Sign in failed.");
        }
    }

    public LiveData<Boolean> getAccountIsValidLiveData() {
        return accountIsValidLiveData;
    }

    public LiveData<GoogleSignInAccount> getAccountLiveData() {
        return accountLiveData;
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }

    public GoogleSignInClient getClient() {
        return client;
    }
}