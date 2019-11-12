package ucsd.ieeeqp.fa19;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import ucsd.ieeeqp.fa19.viewmodel.GoogleSignInViewModel;

public class LoginFragment extends Fragment {
    private static final int RC_GOOGLE_SIGN_IN = 3027;

    private GoogleSignInViewModel gsiViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gsiViewModel = ViewModelProviders.of(getActivity()).get(GoogleSignInViewModel.class);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignInButton signInButton = view.findViewById(R.id.button_main_signin);
        signInButton.setOnClickListener(v -> {
            Intent intent = gsiViewModel.getClient().getSignInIntent();
            startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            gsiViewModel.handleSignInResult(task);
        }
    }
}
