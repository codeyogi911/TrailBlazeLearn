package edu.nus.trailblazelearn.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.RecyclerAdapter;
import edu.nus.trailblazelearn.fragment.SelectTrailDialogFragment;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.DbUtil;

public class ParticipantDefault extends AppCompatActivity implements SelectTrailDialogFragment.NoticeDialogListener {
    private static final String TAG = "PDActivity";
    DbUtil dbUtil = new DbUtil();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private User participant;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.participant_default, menu);
        return true;
    }

    public void toRoleSelect(MenuItem menuItem) {
        participant.revokeParticipant();
        Intent intent = new Intent(getApplicationContext(), RoleSelectActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_default);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        participant = User.getInstance();
        participant.grantParticipant();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                DialogFragment dialogFragment = new SelectTrailDialogFragment();
//                dialogFragment.
                dialogFragment.show(getFragmentManager(), "entertrailcode");
            }
        });

        recyclerView =
                findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, EditText editText) {

        DbUtil.readWithKey("LearningTrail", "trailCode", editText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult().getDocuments();
                            List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                            if (!documentSnapshotList.isEmpty()) {
                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());
                                participant.enrollforTrail(documentSnapshotList.get(0).getId());
                                finish();
                                startActivity(getIntent());

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
}
