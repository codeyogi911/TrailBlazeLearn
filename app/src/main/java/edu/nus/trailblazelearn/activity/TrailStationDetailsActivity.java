package edu.nus.trailblazelearn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.utility.ApplicationConstants;

public class TrailStationDetailsActivity extends AppCompatActivity {
    private final static String TAG = ApplicationConstants.trailStationDetailsActivity;
    Button btnActivities, btnPost;
    TextView stationName, stationInstructions,locationdetails;
    TrailStation trailStationbj;
    StringBuilder stringBuilderName= new StringBuilder("Station Name:");
    StringBuilder stringBuilderInstructions = new StringBuilder("Instructions:");
    Context context;
    Toolbar toolbar;
    FloatingActionButton joinDiscussion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trail_station_details);
        Intent intent = getIntent();
        toolbar = findViewById(R.id.tb_trail_details);
        joinDiscussion = findViewById(R.id.btn_forum);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        trailStationbj = (TrailStation) intent.getSerializableExtra(ApplicationConstants.TrailStation);
        stationName = (TextView) findViewById(R.id.StationName);
        stationInstructions = (TextView) findViewById(R.id.StationInstructions);
        locationdetails=(TextView)findViewById(R.id.StationPlace);
        stringBuilderName.append(trailStationbj.getTrailStationName().toString());
        stringBuilderInstructions.append(trailStationbj.getStationInstructions().toString());
        stationName.setText(trailStationbj.getTrailStationName());
        stationInstructions.setText(trailStationbj.getStationInstructions());
        locationdetails.setText(trailStationbj.getStationAddress());
        getSupportActionBar().setTitle("Station - " + trailStationbj.getStationId());

        joinDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrailStationDetailsActivity.this, StationDiscussionActivity.class);
                intent.putExtra("TrailStation", trailStationbj);
                Log.i(TAG, "call to Post");
                startActivity(intent);
            }
        });
    }

    public void participantListRedirection(View v) {
        Intent intent = new Intent(TrailStationDetailsActivity.this, ParticipantItemListActivity.class);
                intent.putExtra("TrailStation", trailStationbj);
                Log.i(TAG, "call to Activites");
        startActivity(intent);
    }

    public void joinDiscussion(View v) {
        Intent intent = new Intent(TrailStationDetailsActivity.this, StationDiscussionActivity.class);
                intent.putExtra("TrailStation", trailStationbj);
                Log.i(TAG, "call to Post");
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

}
