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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.RecyclerAdapter;
import edu.nus.trailblazelearn.fragment.SelectTrailDialogFragment;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
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
    private Map<String, Object> enrolledTrails = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUI();
        getTrailList().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                enrolledTrails = (Map<String, Object>) documentSnapshot.getData().get(ApplicationConstants.enrolledTrails_key);
                if (enrolledTrails != null) {
                    populateCardList();
                } else {
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void createUI() {
        setContentView(R.layout.activity_participant_default);
        progressBar = findViewById(R.id.progress_bar);
        fragment = findViewById(R.id.recycler_view_fragment);
        textView = findViewById(R.id.trailnotfound_txt);
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
    }

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

    private void populateCardList() {
        final Context that = this;
        trailData = new ArrayList<>();

        Iterator<String> iterator = enrolledTrails.keySet().iterator();
        while (iterator.hasNext()) {
            DbUtil.readWithDocID(ApplicationConstants.learningTrailCollection, iterator.next().toString()).addOnSuccessListener(
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

        final String trailId = editText.getText().toString().toUpperCase();
        DbUtil.readWithDocID("LearningTrail", trailId)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult());
                                User.enrollforTrail(documentSnapshot.getId());
//                                enrolledTrails_key = (Map<String, Object>) documentSnapshot.getData().get("enrolledTrails_key");
                                getTrailList().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        enrolledTrails = (Map<String, Object>) documentSnapshot.getData().get(ApplicationConstants.enrolledTrails_key);
                                        if (enrolledTrails == null) {
                                            enrolledTrails = new HashMap<>();
                                            enrolledTrails.put(trailId, true);
                                        } else {
                                            enrolledTrails.put(trailId, true);
                                        }
                                        populateCardList();
                                    }
                                });
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
                .show();
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

    private Task<DocumentSnapshot> getTrailList() {
        return DbUtil.readWithDocID(ApplicationConstants.usersCollection_key, FirebaseAuth.getInstance().getUid());
    }

    public void signOut(MenuItem menuItem) {
        User.signOut(this);
    }
}
