package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.nus.trailblazelearn.R;
public class RoleSelectActivity extends AppCompatActivity {
    private static final String TAG = "RSActivity";
    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Auto redirection to trainer or participant
        FirebaseFirestore.getInstance().collection("users").document(mAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        if (document.get("isTrainer") != null && (boolean) task.getResult().get("isTrainer")) {
                            loginTrainer(null);
                        } else if (document.get("isParticipant") != null && (boolean) task.getResult().get("isParticipant")) {
                            loginParticipant(null);
                        } else {
                            createUI();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                        createUI();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void createUI() {
        setContentView(R.layout.activity_role_select);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void loginTrainer(View view) {
        Intent intent = new Intent(this, TrainerDefault.class);
        startActivity(intent);
        finish();
    }

    public void loginParticipant(View view) {
        Intent intent = new Intent(this, ParticipantDefault.class);
        startActivity(intent);
        finish();
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
    }

    private void updateUI() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
