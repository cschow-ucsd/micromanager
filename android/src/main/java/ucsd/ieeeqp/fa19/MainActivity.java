package ucsd.ieeeqp.fa19;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ucsd.ieeeqp.fa19.service.MmService;
import ucsd.ieeeqp.fa19.service.MmServiceUtil;
import ucsd.ieeeqp.fa19.service.TestResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int RC_SIGN_IN = 3027;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oauth_client_id))
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.button_main_signin);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.button_main_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mGoogleSignInClient);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(final GoogleSignInAccount account) {
        // TODO: update UI after verified login with backend
        if (account == null) {
            Toast.makeText(this, "No account", Toast.LENGTH_SHORT).show();
        } else if (account.getIdToken() == null) {
            Toast.makeText(this, "No token", Toast.LENGTH_SHORT).show();
        } else {
//            MmServiceUtil.service.getProtected(
//                    new MmClientGoogleAuthToken(account.getIdToken())
//            ).enqueue(new Callback<TestResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<TestResponse> call, @NotNull Response<TestResponse> response) {
//                    Log.d(TAG, "onResponse: " + response.body());
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<TestResponse> call, @NotNull Throwable t) {
//                    Log.e(TAG, "onFailure: Backend server authentication failed", t);
//                }
//            });
        }
        //launch new activity
    }

    private void signIn(GoogleSignInClient client) {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "handleSignInResult: email: " + account.getEmail());
                Log.d(TAG, "handleSignInResult: ID token: " + account.getIdToken());
            }
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult: failed code=" + e.getStatusCode() + "; message=" + e.getMessage());
            updateUI(null);
        }
    }
}
