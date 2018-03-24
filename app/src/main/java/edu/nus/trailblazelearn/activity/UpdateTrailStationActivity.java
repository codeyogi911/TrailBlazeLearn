package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Random;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.exception.TrailDaoException;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DbUtil;

import static edu.nus.trailblazelearn.utility.ApplicationConstants.trailStation;

public class UpdateTrailStationActivity extends AppCompatActivity {
    private static final String TAG = ApplicationConstants.updateTrailStationActivity;
    Toolbar toolbar;
    private EditText edstationName, edinstructions;
    private TextView locationDetails;
    private Button btnUpdate;
    private FloatingActionButton btnSearch;
    private String trailCode,address,gps,gpsEdit;
    private Integer stationId,sequence,stationSize;
    private boolean editStation, isTrainer;
    private LatLng stationLocation;
    private User user;
    private double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trail_station);
        final TrailStation editStationObj = (TrailStation) getIntent().getSerializableExtra(ApplicationConstants.stationName);
        if (editStationObj != null)
            editStation = true;
        if (editStationObj == null) {
            trailCode = (String) getIntent().getSerializableExtra(ApplicationConstants.trailCodeMap);
            stationSize = (Integer) getIntent().getSerializableExtra(ApplicationConstants.stationSize);
            stationLocation = (LatLng) getIntent().getParcelableExtra(ApplicationConstants.stationLocation);
            address = (String) getIntent().getSerializableExtra(ApplicationConstants.address);
            stationId = (Integer) getIntent().getSerializableExtra("stationId");
        }
        toolbar = (Toolbar) findViewById(R.id.trail_header);
        setSupportActionBar(toolbar);
        edstationName = (EditText) findViewById(R.id.station_name);
        edinstructions = (EditText) findViewById(R.id.station_instructions);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnSearch = (FloatingActionButton) findViewById(R.id.btn_search);
        locationDetails = (TextView) findViewById(R.id.getEditPlace);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Trail Station");
        btnUpdate.setText(getString(R.string.update));
        if (editStationObj != null) {
            edstationName.setText(editStationObj.getTrailStationName());
            edinstructions.setText(editStationObj.getStationInstructions());
            locationDetails.setText(editStationObj.getStationAddress());
            stationId = editStationObj.getStationId();
            trailCode = editStationObj.getTrailCode();
            sequence = editStationObj.getSequence();
            gps = editStationObj.getGps();
            address = editStationObj.getStationAddress();
            if (gps != null) {
                gpsEdit = gps;
                gpsEdit = gpsEdit.replace("lat/lng:", "");
                gpsEdit = gpsEdit.replace("(", "");
                gpsEdit = gpsEdit.replace(")", "");
                String[] strLatLong = gpsEdit.split("\\|");
                for (String item : strLatLong) {
                    String[] str = item.split(",");
                    latitude = Double.parseDouble(str[0]);
                    longitude = Double.parseDouble(str[1]);
                }
            }
        }
        else
        {
            locationDetails.setText(address);
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(UpdateTrailStationActivity.this, MapsActivity.class);
                intent.putExtra(ApplicationConstants.trailCodeMap, trailCode);
                intent.putExtra(ApplicationConstants.stationSize, stationSize);
                intent.putExtra("stationId", stationId);
                intent.putExtra("editMode", true);
                if (gps != null) {
                    intent.putExtra(ApplicationConstants.lat, latitude);
                    intent.putExtra(ApplicationConstants.lon, longitude);
                    intent.putExtra("Address", address);
                }

                startActivity(intent);
                finish();
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stationName = ((EditText) findViewById(R.id.station_name)).getText().toString().trim();
                String instructions = ((EditText) findViewById(R.id.station_instructions)).getText().toString().trim();
                locationDetails.setText(address);
                TrailStation trailStationObj = new TrailStation();
                trailStationObj.setTrailStationName(stationName);
                trailStationObj.setStationInstructions(instructions);
                trailStationObj.setTrailCode(trailCode);
                trailStationObj.setStationAddress(address);
                trailStationObj.setGps(gps);
                trailStationObj.setStationId(stationId);

                if (TextUtils.isEmpty(stationName)) {
                    edstationName.setError("Please enter the StationName");
                    return;
                }

                if (TextUtils.isEmpty(instructions)) {
                    edinstructions.setError("Please enter the instructions");
                    return;
                }

                if (address == null) {
                    Toast.makeText(UpdateTrailStationActivity.this, "Please select the location", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    updateStation(trailStationObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
        private void updateStation(TrailStation trailStation) throws Exception {
            stationId = trailStation.getStationId();

            try {
                DbUtil.addRecordForCollection("TrailStation", trailStation, stationId.toString());
                Toast.makeText(UpdateTrailStationActivity.this, "Station Updated Successfully", Toast.LENGTH_SHORT).show();
            } catch (TrailDaoException daoException) {
                throw new Exception("Error occurred in Create Station invoking addRecordForCollection ", daoException);
            }
            Intent intent = new Intent(getApplicationContext(), TrailStationListActivity.class);
            intent.putExtra(ApplicationConstants.trailCodeMap, trailCode);
            startActivity(intent);
            finish();
        }
    }


