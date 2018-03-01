package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import edu.nus.trailblazelearn.*;
import edu.nus.trailblazelearn.model.User;

public class RoleSelectActivity extends AppCompatActivity {


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
//        new User(

    }

    public void loginTrainer(View view) {
        Intent intent = new Intent(this, TrainerDefault.class);
        startActivity(intent);
        finish();
    }

    public void loginParticipant(View view) {

        Intent intent = new Intent(this, ParticipantDefault.class);
//        intent.putExtra("participant",new Participant(userData));
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
