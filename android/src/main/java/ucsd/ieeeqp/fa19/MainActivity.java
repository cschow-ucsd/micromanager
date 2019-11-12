package ucsd.ieeeqp.fa19;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import ucsd.ieeeqp.fa19.model.GoogleSignInViewModel;
import ucsd.ieeeqp.fa19.model.MmServiceViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleSignInViewModel gsiViewModel;
    private MmServiceViewModel mmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // view model setup
        gsiViewModel = ViewModelProviders.of(this).get(GoogleSignInViewModel.class);
        mmViewModel = ViewModelProviders.of(this).get(MmServiceViewModel.class);
        gsiViewModel.getGoogleLoginStateLiveData().observe(this, this::handleGoogleAccountStateChange);
        mmViewModel.getMmLoginStateLiveData().observe(this, this::handleMmStateChange);
    }

    private void handleGoogleAccountStateChange(int googleStateCode) {
        switch (googleStateCode) {
            case GoogleSignInViewModel.ACCOUNT_FINDING:
                // Do nothing here because it is covered by handleMmStateChange
                Log.d(TAG, "handleGoogleAccountStateChange: Finding Google account...");
                break;
            case GoogleSignInViewModel.ACCOUNT_FOUND:
                mmViewModel.initService(gsiViewModel.getAccount().getServerAuthCode());
                mmViewModel.loginToApi();
                break;
            case GoogleSignInViewModel.ACCOUNT_NOT_FOUND:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_main_container, new LoginFragment())
                        .commit();
                break;
            case GoogleSignInViewModel.ACCOUNT_LOGIN_FAILED:
                Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_main_container, new LoginFragment())
                        .commit();
        }
    }

    private void handleMmStateChange(int mmStateCode) {
        switch (mmStateCode) {
            case MmServiceViewModel.NOT_LOGGED_IN:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_main_container, new LoadingFragment())
                        .commit();
                break;
            case MmServiceViewModel.LOGIN_SUCCESS:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_main_container, new NavigationFragment())
                        .commit();
                break;
            case MmServiceViewModel.LOGIN_FAILED:
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_main_container, new LoginFragment())
                        .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gsiViewModel.findExistingAccount();
    }
}
