package edu.nus.trailblazelearn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import edu.nus.trailblazelearn.R;
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
        createUI();
    }

    private void createUI() {
        setContentView(R.layout.activity_role_select);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        CardView trainerCard = findViewById(R.id.card_view_trainer);
        CardView participantCard = findViewById(R.id.card_view_participant);
        trainerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTrainer();
            }
        });
        participantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginParticipant();
            }
        });
    }

    public void loginTrainer() {
        User.loginasTrainer(this);
    }

    public void loginParticipant() {
        User.loginasParticipant(this);
    }

    public void signOut(MenuItem menuItem) {
        User.signOut(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LearningTrailListActivity.this.finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
