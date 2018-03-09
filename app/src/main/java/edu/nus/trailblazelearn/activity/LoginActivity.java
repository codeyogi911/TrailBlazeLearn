package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity.Class";
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
    );
    private FirebaseAuth mAuth;
    private User loggedInUser;

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
        loggedInUser = new User(this);
        loggedInUser.populateData().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.get("isTrainer") != null && (boolean) task.getResult().get("isTrainer")) {
                            loginTrainer();
                        } else if (documentSnapshot.get("isParticipant") != null && (boolean) task.getResult().get("isParticipant")) {
                            loginParticipant();
                        } else {
                            loginRoleSelect();
                        }
                    } else
                        loginRoleSelect();

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
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
                // Successfully signed in
//                FirebaseUser user = mAuth.getCurrentUser();
                navToWelcome();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

    private void loginTrainer() {
        Intent intent = new Intent(this, TrainerDefault.class);
        startActivity(intent);
        finish();
    }

    private void loginParticipant() {
        Intent intent = new Intent(this, ParticipantDefault.class);
        startActivity(intent);
        finish();
    }

    private void loginRoleSelect() {
        Intent intent = new Intent(getApplicationContext(), RoleSelectActivity.class);
        startActivity(intent);
        finish();
    }
}


