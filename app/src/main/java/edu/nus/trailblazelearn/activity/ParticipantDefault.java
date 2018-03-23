package edu.nus.trailblazelearn.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.RecyclerAdapter;
import edu.nus.trailblazelearn.fragment.SelectTrailDialogFragment;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.DbUtil;

public class ParticipantDefault extends AppCompatActivity implements SelectTrailDialogFragment.NoticeDialogListener {
    private static final String TAG = "PDActivity";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ProgressBar progressBar;
    List<DocumentSnapshot> trailData = new ArrayList<>();
    View fragment;
    TextView textView;
    private User participant;
    private List<String> enrolledTrails = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.participant_default, menu);
        MenuItem trainer = menu.findItem(R.id.trainer_icon);
        trainer.setVisible(false);
        return true;
    }

    public void toRoleSelect(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(), RoleSelectActivity.class);
        startActivity(intent);
        finish();
    }

    public void signOut(MenuItem menuItem) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        edu.nus.trailblazelearn.model.User.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_default);
        participant = User.getInstance(this);
        participant.grantParticipant();
        enrolledTrails = (List<String>) participant.getData().get("enrolledTrails");
        progressBar = findViewById(R.id.progress_bar);
        fragment = findViewById(R.id.recycler_view_fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new SelectTrailDialogFragment();
                dialogFragment.show(getFragmentManager(), "entertrailcode");
            }
        });
        textView = findViewById(R.id.trailnotfound_txt);
        if (enrolledTrails != null) {
            populateCardList();
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    private void populateCardList() {
        final Context that = this;
        trailData = new ArrayList<>();
        for (int i = 0; i < enrolledTrails.size(); i++) {
            FirebaseFirestore.getInstance().collection("LearningTrail").document(enrolledTrails.get(i)).get().addOnSuccessListener(
                    new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                trailData.add(documentSnapshot);
                                if (trailData.size() == enrolledTrails.size()) {
                                    progressBar.setVisibility(View.GONE);
                                    fragment.setVisibility(View.VISIBLE);
                                    recyclerView = findViewById(R.id.recycler_view);
                                    layoutManager = new LinearLayoutManager(that);
                                    recyclerView.setLayoutManager(layoutManager);
                                    textView.setVisibility(View.GONE);
                                    adapter = new RecyclerAdapter(that, trailData);
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, EditText editText) {

        DbUtil.readWithDocID("LearningTrail", editText.getText().toString().toUpperCase())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult());
                                participant.enrollforTrail(documentSnapshot.getId());
                                enrolledTrails = (List<String>) User.getInstance().getData().get("enrolledTrails");
                                populateCardList();
                            } else {
                                Log.d(TAG, "No such trail, try creating new trail");
                                Snackbar.make(findViewById(R.id.participantdefault), "Trail not found!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } else {
//                    Add exception handling
                            Log.d(TAG, "get failed with ", task.getException());
                            Snackbar.make(findViewById(R.id.participantdefault), "Cannot access internet, please check data connection!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Snackbar.make(findViewById(R.id.participantdefault), "Enter a trail code to continue!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
