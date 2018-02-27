package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


import java.util.Arrays;

import edu.nus.trailblazelearn.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    CallbackManager callbackManager = CallbackManager.Factory.create();
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private AccessToken fbToken = AccessToken.getCurrentAccessToken();
    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account
        GoogleSignInAccount gToken = GoogleSignIn.getLastSignedInAccount(this);
        //Check for Fb login
        if (fbToken != null || gToken != null) {
//            setContentView(R.layout.activity_main);
            updateUI(gToken, fbToken);
        } else {
            handleUserLogin();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }

    private void updateUI(GoogleSignInAccount account) {
        updateUI(account, null);
    }

    private void updateUI(GoogleSignInAccount account, AccessToken FbAccount) {
        Intent intent = new Intent(this, RoleSelectActivity.class);
        intent.putExtra("gToken", account);
        intent.putExtra("fbToken", FbAccount);
//        intent.putExtra("gSignInClient",mGoogleSignInClient);
        startActivity(intent);
        finish();

    }


    private void handleUserLogin() {
        final String EMAIL = "email";
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                fbToken = loginResult.getAccessToken();
                Toast.makeText(getApplicationContext(), "Facebook Login Successful", Toast.LENGTH_LONG).show();

                updateUI(null, fbToken);

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "Please login to continue...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.println(Log.ERROR, "err", exception.toString());
            }

        });
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

//        SignInButton signInButton1 = (SignInButton) findViewById(R.id.sign_in_button);

//        findViewById(R.id.sign_in_button).setOnClickListener(getApplicationContext());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
//            case R.id.sign_out_button:
//                signOut();
//                break;
//            case R.id.disconnect_button:
//                revokeAccess();
//                break;
        }
    }
}


