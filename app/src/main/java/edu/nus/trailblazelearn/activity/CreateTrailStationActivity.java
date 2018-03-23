package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.exception.TrailDaoException;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DbUtil;


public class CreateTrailStationActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "sg.edu.nus.trailstation.MESSAGE ";
    private static final String TAG = ApplicationConstants.createTrailStationActivity;
    public static FirebaseFirestore firebaseStorage;
    private static StorageReference storageReference;
    Toolbar toolbar;
    List<TrailStation> trailStationList;
    private LatLng stationLocation;
    private EditText edstationName, edinstructions;
    private TextView locationDetails;
    private Button btnSave;
    private FloatingActionButton btnSearch;
    private String trailCode,address;
    private Integer stationId,sequence,stationSize;
    private boolean editStation, isTrainer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trail_station);
        user = User.getInstance(this);
        isTrainer = (boolean) user.getData().get("isTrainer");
        toolbar = (Toolbar) findViewById(R.id.trail_header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Get the values from Intent passed
            trailCode = (String) getIntent().getSerializableExtra(ApplicationConstants.trailCode);
            if(trailCode==null)
                trailCode=(String) getIntent().getSerializableExtra(ApplicationConstants.trailCodeMap);
            stationSize = (Integer) getIntent().getSerializableExtra(ApplicationConstants.stationSize);
            stationLocation = (LatLng) getIntent().getParcelableExtra(ApplicationConstants.stationLocation);
            address = (String) getIntent().getSerializableExtra(ApplicationConstants.address);

        edstationName = (EditText) findViewById(R.id.station_name);
        edinstructions = (EditText) findViewById(R.id.station_instructions);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSearch = (FloatingActionButton) findViewById(R.id.btn_search);
        locationDetails=(TextView) findViewById(R.id.getPlace);
        if(address!=null)
            locationDetails.setText(address);

        firebaseStorage = firebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnSearch.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(CreateTrailStationActivity.this, MapsActivity.class);
                intent.putExtra(ApplicationConstants.trailCodeMap,trailCode);
                intent.putExtra(ApplicationConstants.stationSize, stationSize);
                startActivity(intent);

            }
        });
        final TrailStation editStationObj = (TrailStation) getIntent().getSerializableExtra(ApplicationConstants.stationName);
        if (editStationObj != null)
            editStation = true;

        if (!editStation) {
            btnSave.setText(getString(R.string.save));
        } else {
            btnSave.setText(getString(R.string.update));
        }
        if (editStation) {
            Log.d(TAG, "Edit Trail Station for" + editStationObj.getTrailCode());
            edstationName.setText(editStationObj.getTrailStationName());
            edinstructions.setText(editStationObj.getStationInstructions());
            locationDetails.setText(editStationObj.getStationAddress());
            stationId=editStationObj.getStationId();
            trailCode = editStationObj.getTrailCode();
            sequence=editStationObj.getSequence();
            stationLocation=editStationObj.getLatLng();
        }

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String stationName = ((EditText) findViewById(R.id.station_name)).getText().toString().trim();
                String instructions = ((EditText) findViewById(R.id.station_instructions)).getText().toString().trim();
                TrailStation trailStationObj = new TrailStation();
                trailStationObj.setTrailStationName(stationName);
                trailStationObj.setStationInstructions(instructions);
                trailStationObj.setTrailCode(trailCode);
                trailStationObj.setStationAddress(address);
                trailStationObj.setLatLng(stationLocation);

                if (TextUtils.isEmpty(stationName)) {
                    Toast.makeText(CreateTrailStationActivity.this, "You must enter the Station Name", Toast.LENGTH_LONG).show();
                    edstationName.setError("Please enter the StationName");
                    return;
                }

                if (TextUtils.isEmpty(instructions)) {
                    Toast.makeText(CreateTrailStationActivity.this, "You must enter Instructions for the Station", Toast.LENGTH_LONG).show();
                    edinstructions.setError("Please enter the instructions");
                    return;
                }

                if(address==null){
                    Toast.makeText(CreateTrailStationActivity.this, "Please select the location", Toast.LENGTH_LONG).show();
                    return;
                }


                if (!editStation) {
                    try {
                        getSupportActionBar().setTitle("Create Trail Station for" +trailStationObj.getTrailCode());
                        createStation(trailStationObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getSupportActionBar().setTitle("Update Trail Station for " + trailStationObj.getTrailCode());
                    try {
                        updateTrailStation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_role_icon, menu);
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

    /**
     * Creating new Trail Station node under 'Learning Trail'
     */
    private void createStation(TrailStation trailStation) throws Exception {
        // TODO
        if(!editStation) {
            Random random = new Random();
            stationId = random.nextInt(10000);
            trailStation.setStationId(stationId);
            trailStation.setSequence(stationSize+1);
        }else
        {stationId= trailStation.getStationId();}

        Log.d("stationId", stationId.toString());
        if (!TextUtils.isEmpty(stationId.toString())) {
            try {
                DbUtil.addRecordForCollection("TrailStation", trailStation, stationId.toString());
                if (!editStation)
                    Toast.makeText(CreateTrailStationActivity.this, "Station Created Successfully", Toast.LENGTH_SHORT).show();
            }
            catch(TrailDaoException daoException)
            {
                throw new Exception("Error occurred in Create Station invoking addRecordForCollection ", daoException);
            }
        Intent intent=new Intent(getApplicationContext(),TrailStationListActivity.class);
            intent.putExtra(ApplicationConstants.trailCode,trailCode);
            startActivity(intent);

        }
    }

    private void updateTrailStation() throws Exception{
        Log.d(TAG, "Start updateTrailStation ");
        TrailStation updateStationObj = showStationDetails();
        try {
            /**Construct Helper to call DB and persist data**/
            createStation(updateStationObj);
            Toast.makeText(CreateTrailStationActivity.this, "Updated Successfully",
                    Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            throw new Exception("Error Occured when updating the station", e);
        }

    }

    private TrailStation showStationDetails() {
        Log.d(TAG, "display of created station details");
        TrailStation trailStationObj = new TrailStation();
        trailStationObj.setTrailStationName(edstationName.getText().toString());
        trailStationObj.setStationInstructions(edinstructions.getText().toString());
        trailStationObj.setStationId(stationId);
        if (trailCode != null) {
            trailStationObj.setTrailCode(trailCode);
        }
        return trailStationObj;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

}
