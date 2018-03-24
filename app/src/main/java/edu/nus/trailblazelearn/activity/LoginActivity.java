package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.exception.NetworkError;
import edu.nus.trailblazelearn.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity.CLASS";

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.FacebookBuilder().build()))
                            .setTheme(R.style.ToolbarTheme)
                            .setLogo(R.mipmap.ic_logo)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);
        } else {
            navToWelcome();
        }
    }

    private void navToWelcome() {
        User.initialize()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                                User.setData(document.getData());
                            } else {
                                Log.d(TAG, "No such document, creating new user in DB");
                                Map<String, Object> temp = new HashMap<>();
                                FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                                temp.put("name", mAuth.getDisplayName());
                                temp.put("email", mAuth.getEmail());
                                User.setData(temp);
                                User.save().addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        startActivity(new Intent(getApplicationContext(), NetworkError.class));
                                    }
                                });
                            }
                            redirect();
                        } else {
//                    Add exception handling
                            Log.d(TAG, "get failed with ", task.getException());
                            startActivity(new Intent(getApplicationContext(), NetworkError.class));
                            finish();
                        }
                    }
                });
    }

    private void redirect() {
        if (User.getData().get("isTrainer") != null && (boolean) User.getData().get("isTrainer")) {
            User.loginTrainer(this);
        } else if (User.getData().get("isParticipant") != null && (boolean) User.getData().get("isParticipant")) {
            User.loginParticipant(this);
        } else {
            User.loginRoleSelect(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                navToWelcome();
            } else {
                Snackbar.make(findViewById(R.id.participantdefault), "Sign in failed, Please try again using different method!", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }
}


