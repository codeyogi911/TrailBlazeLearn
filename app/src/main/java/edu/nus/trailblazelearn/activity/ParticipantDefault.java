package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.Participant;

public class ParticipantDefault extends AppCompatActivity {
    Map<String, Object> userData = new HashMap<>();
    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.participant_default, menu);
        return true;
    }

    public void toRoleSelect(MenuItem menuItem) {
        Intent intent = new Intent(this,
                RoleSelectActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_default);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        userData.put("name", mAuth.getDisplayName());
        userData.put("email", mAuth.getEmail());
        userData.put("uid", mAuth.getUid());
        userData.put("isParticipant", true);
        Participant participant = new Participant();
    }

}
