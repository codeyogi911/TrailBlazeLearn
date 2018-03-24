package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.ParticipantItemAdapter;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DbUtil;

public class ParticipantItemListActivity extends AppCompatActivity {
    private static final String TAG = ApplicationConstants.participantItemListActivity;
    private ParticipantItemAdapter participantItemAdapter;
    private ArrayList<ParticipantItem> participantItemArrayList = new ArrayList<>();
    private TrailStation trailStation = new TrailStation();
    private LearningTrail learningTrail = new LearningTrail();
    User user = User.getInstance(this);
    public boolean isTrainer = (boolean) user.getData().get("isTrainer");
    public boolean isParticipant = (boolean) user.getData().get("isParticipant");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_item_list);
        Log.d(TAG,"Start of onCreate participant List Activity");
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        final FloatingActionButton floatingActionButton = findViewById(R.id.add_item_ActionButton);
        Toolbar toolbar = findViewById(R.id.tb_participant_item_list_header);
        final TextView notFound = findViewById(R.id.items_not_found);
        final TextView stationName = findViewById(R.id.station_name_in_activity);
        FloatingActionButton joinDiscussion = findViewById(R.id.btn_forum);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.participant_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        participantItemAdapter = new ParticipantItemAdapter(getApplicationContext(), participantItemArrayList);
        recyclerView.setAdapter(participantItemAdapter);
        trailStation = (TrailStation) getIntent().getSerializableExtra("TrailStation");

        getSupportActionBar().setTitle("Activities - " + trailStation.getStationId());
        stationName.setText(trailStation.getTrailStationName());

        String trailCode = trailStation.getTrailCode();
        DbUtil.readWithDocID("LearningTrail",trailCode).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Date currentDate = new Date();
                    Date endDate = (Date) documentSnapshot.get("endDate");
                    if (isParticipant && endDate.before(currentDate)) {
                        stationName.setText(trailStation.getTrailStationName() + "(EXPIRED)");
                        stationName.setTextColor(Color.RED);
                        floatingActionButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        if(isTrainer) {
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        /*Date endDate = learningTrail.getEndDate();
        Date currentDate = new Date();
        boolean value = endDate.before(currentDate);
*/

        FirebaseFirestore.getInstance().collection("participantActivities")
                .whereEqualTo("trailStationId",trailStation.getStationId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null) {
                    Log.d("ParticipantItemList", e.getMessage());
                }
                participantItemArrayList.clear();
                for(DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                    ParticipantItem participantItem = documentSnapshot.toObject(ParticipantItem.class);
                    participantItemArrayList.add(participantItem);
                    notFound.setVisibility(View.GONE);
                }
                participantItemAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });

        joinDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantItemListActivity.this, StationDiscussionActivity.class);
                intent.putExtra("TrailStation", trailStation);
                Log.i(TAG, "call to Post");
                startActivity(intent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantItemListActivity.this, ParticipantAddItemActivity.class);
                intent.putExtra("TrailStation", trailStation);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.participant_default, menu);
        MenuItem participant = menu.findItem(R.id.participant_icon);
        MenuItem trainer = menu.findItem(R.id.trainer_icon);
        if(isTrainer) {
            participant.setVisible(false);
        }
        else {
            trainer.setVisible(false);
        }
        return true;
    }

    public void toRoleSelect(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(),
                RoleSelectActivity.class);
        startActivity(intent);
        finish();
    }

    public void onIconSelect(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(),
                UserProfileActivity.class);
        startActivity(intent);
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
                });    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

}
