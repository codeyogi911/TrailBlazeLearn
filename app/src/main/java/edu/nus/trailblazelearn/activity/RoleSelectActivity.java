package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import edu.nus.trailblazelearn.*;

public class RoleSelectActivity extends AppCompatActivity {
    String welcomeMsg;
    GoogleSignInAccount gToken;
    ProfileTracker mProfileTracker;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
//        Intent intent = getIntent();
//        FirebaseAuth user = intent.getParcelableExtra("user");

//        gToken = (GoogleSignInAccount) bundle.get("gToken");
//        if (gToken != null) {
//            welcomeMsg = "Welcome " + gToken.getDisplayName();
//        } else {
//            if (Profile.getCurrentProfile() != null) {
//                welcomeMsg = "Welcome " + Profile.getCurrentProfile().getName();
//            }
//             mProfileTracker = new ProfileTracker() {
//                @Override
//                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                    // Fetch user details from New Profile
//                    welcomeMsg = "Welcome "+ Profile.getCurrentProfile().getName();
//                    TextView textView = findViewById(R.id.welcome_msg);
//                    textView.setText(welcomeMsg);
//                    mProfileTracker.stopTracking();
//                }
//            };
//            mProfileTracker.startTracking();
//        }
//        TextView textView = findViewById(R.id.welcome_msg);
//        textView.setText(welcomeMsg);
    }

    public void signOut(MenuItem menuItem) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI();
                        // ...
                    }
                });
//        if (gToken != null) {
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestEmail()
//                    .build();
//            GoogleSignIn.getClient(this, gso).signOut()
//                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // [START_EXCLUDE]
//                            updateUI(null);
//                            // [END_EXCLUDE]
//                        }
//                    });
//        } else {
//            LoginManager.getInstance().logOut();
//            updateUI(null);
//        }
    }

    private void updateUI() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
