package ucsd.ieeeqp.fa19;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import ucsd.ieeeqp.fa19.model.GoogleSignInViewModel;
import ucsd.ieeeqp.fa19.model.MmServiceViewModel;
import ucsd.ieeeqp.fa19.service.TestResponse;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 3027;

    private GoogleSignInViewModel gsiViewModel;
    private MmServiceViewModel mmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // view model setup
        gsiViewModel = ViewModelProviders.of(this).get(GoogleSignInViewModel.class);
        mmViewModel = ViewModelProviders.of(this).get(MmServiceViewModel.class);
        setupViewModelObservers();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.button_main_signin);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.button_main_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(gsiViewModel.getClient());
            }
        });
    }

    private void setupViewModelObservers() {
        gsiViewModel.getAccountIsValidLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isValid) {
                if (isValid) {
                    String idToken = gsiViewModel.getAccountLiveData().getValue().getIdToken();
                    mmViewModel.initService(idToken);
                    mmViewModel.fetchProtectedDataAsync();
                }
                // TODO: If not valid then need to show the user
            }
        });
        mmViewModel.protectedDataLiveData().observe(this, new Observer<TestResponse>() {
            @Override
            public void onChanged(TestResponse testResponse) {
                if (testResponse != null) {
                    Log.d(TAG, "onChanged: " + testResponse.getBody()); //should return "It's protected!"
                }
                // TODO: show protected data on screen
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gsiViewModel != null) {
            gsiViewModel.findExistingAccount();
        }
    }

    private void updateUI(final GoogleSignInAccount account) {
        // TODO: update UI after verified login with backend
        if (account == null) {
            Toast.makeText(this, "No account", Toast.LENGTH_SHORT).show();
        } else if (account.getIdToken() == null) {
            Toast.makeText(this, "No token", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: update UI
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

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            gsiViewModel.handleSignInResult(task);
        }
    }
}
