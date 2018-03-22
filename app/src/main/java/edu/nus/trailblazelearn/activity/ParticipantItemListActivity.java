package edu.nus.trailblazelearn.activity;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.ParticipantItemAdapter;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.utility.ApplicationConstants;

public class ParticipantItemListActivity extends AppCompatActivity {
    private static final String TAG = ApplicationConstants.participantItemListActivity;
    private ParticipantItemAdapter participantItemAdapter;
    private ArrayList<ParticipantItem> participantItemArrayList = new ArrayList<>();
    private TrailStation trailStation = new TrailStation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_item_list);
        Log.d(TAG,"Start of onCreate participant List Activity");
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_item_ActionButton);
        Toolbar toolbar = findViewById(R.id.tb_participant_item_list_header);
        TextView stationName = findViewById(R.id.station_name_in_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.participant_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //ArrayList<ParticipantItem> participantItemArrayList = dbUtil.getParticipantItems("Ragu", progressBar, );
        participantItemAdapter = new ParticipantItemAdapter(getApplicationContext(), participantItemArrayList);
        recyclerView.setAdapter(participantItemAdapter);
        trailStation = (TrailStation) getIntent().getSerializableExtra("TrailStation");
        getSupportActionBar().setTitle("Activities - " + trailStation.getStationId());
        stationName.setText(trailStation.getTrailStationName());

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
                }
                participantItemAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
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
        return true;
    }

    public void toRoleSelect(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(),
                RoleSelectActivity.class);
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
                });    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

}
