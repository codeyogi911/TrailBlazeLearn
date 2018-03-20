package edu.nus.trailblazelearn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.TrailStation;

public class TrailStationDetailsActivity extends AppCompatActivity {
    private final static String TAG = "Station Details Activity";
    Button btnActivities, btnPost;
    TextView stationName, stationInstructions;
    TrailStation trailStationbj;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trail_station_details);
        Intent intent = getIntent();
        toolbar = findViewById(R.id.tb_trail_details);
        trailStationbj = (TrailStation) intent.getSerializableExtra("TrailStation");
        stationName = (TextView) findViewById(R.id.StationName);
        stationInstructions = (TextView) findViewById(R.id.StationInstructions);
        stationName.setText(trailStationbj.getTrailStationName().toString());
        stationInstructions.setText(trailStationbj.getStationInstructions().toString());
        toolbar.setTitle("Trail Station");
    }

    public void participantListRedirection(View v) {
        Intent intent = new Intent(getApplicationContext(), ParticipantItemListActivity.class);
                intent.putExtra("TrailStation", trailStationbj);
                Log.i(TAG, "call to Activites");
        startActivity(intent);
    }

    public void joinDiscussion(View v) {
        Intent intent = new Intent(getApplicationContext(), StationDiscussionActivity.class);
                intent.putExtra("TrailStation", trailStationbj);
                Log.i(TAG, "call to Post");
        startActivity(intent);
    }

}
