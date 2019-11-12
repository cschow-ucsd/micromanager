package ucsd.ieeeqp.fa19;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ucsd.ieeeqp.fa19.viewmodel.GoogleSignInViewModel;
import ucsd.ieeeqp.fa19.viewmodel.MmServiceViewModel;

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

        // find existing account
        gsiViewModel.findExistingAccount();
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
                break;
        }
    }

    private void handleMmStateChange(int mmStateCode) {
        Fragment fragment = null;
        switch (mmStateCode) {
            case MmServiceViewModel.NOT_LOGGED_IN:
            case MmServiceViewModel.LOGGING_IN:
                fragment = new LoadingFragment();
                break;
            case MmServiceViewModel.LOGIN_SUCCESS:
                fragment = new NavigationFragment();
                break;
            case MmServiceViewModel.LOGIN_FAILED:
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                fragment = new LoginFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_main_container, fragment)
                    .commit();
        }
    }
}
