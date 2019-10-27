package ucsd.ieeeqp.fa19;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

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
                .requestProfile()
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
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        } else {
            Runnable serverLogin = new Runnable() {

                @Override
                public void run() {
                    String idToken = account.getIdToken();
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost(MmApiUtils.LOGIN_URL);
                    try {
                        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                        nameValuePairs.add(new BasicNameValuePair("id", idToken));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        ClassicHttpResponse response = httpClient.execute(httpPost);
                        final String body = EntityUtils.toString(response.getEntity());
                        Log.d(TAG, "HttpResponse: " + body);
                    } catch (Exception e) {
                        Log.e(TAG, "HttpResponse exception: " + e.getMessage(), e);
                    }
                }
            };
            new Thread(serverLogin).start();
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

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
}
