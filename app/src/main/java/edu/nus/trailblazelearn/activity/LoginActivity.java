package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    //    private static final String TAG = "LoginActivity.Class";
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(), new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
    );
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
                            .build(),
                    RC_SIGN_IN);
//            finish();
        } else {
            navToWelcome();
        }
    }

    private void navToWelcome() {
        User.getInstance(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                navToWelcome();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }
}


