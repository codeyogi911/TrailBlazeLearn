package edu.nus.trailblazelearn;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.DbUtil;

public class UserProfileActivity extends AppCompatActivity {

    private User user;
    private String username;
    private String userEmail;
    private boolean isTrainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Toolbar toolbar = findViewById(R.id.profile_header);
        TextView userProfileName = findViewById(R.id.user_name);
        final TextView role = findViewById(R.id.role);
        Button okButton = findViewById(R.id.okay);
        user = User.getInstance(this);
        username = (String) user.getData().get("name");
        isTrainer = (boolean) user.getData().get("isTrainer");
        if(isTrainer) {
            role.setText("TRAINER");
        }
        else {
            role.setText("PARTICIPANT");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userProfileName.setText(username);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
