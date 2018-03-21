package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.exception.TrailDaoException;
import edu.nus.trailblazelearn.exception.TrailHelperException;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.dbUtil;

import static edu.nus.trailblazelearn.utility.DateUtil.constructDateToStringDate;


public class CreateTrailStationActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "sg.edu.nus.trailstation.MESSAGE ";
    private static final String TAG = "CreateTrailStation";
    public static FirebaseFirestore firebaseStorage;
    private static StorageReference storageReference;
    private EditText edstationName, edinstructions;
    private TextView txtDetails;
    private Button btnSave, btnSearch;
    private String trailCode;
    private Integer stationId;
    private boolean editStation;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trail_station);
        toolbar = (Toolbar) findViewById(R.id.trail_header);

        //Get the trailCode from Intent passed
        trailCode = (String) getIntent().getSerializableExtra(ApplicationConstants.trailCode);

        edstationName = (EditText) findViewById(R.id.station_name);
        edinstructions = (EditText) findViewById(R.id.station_instructions);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSearch = (Button) findViewById(R.id.btn_search);

        firebaseStorage = firebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnSearch.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(CreateTrailStationActivity.this, MapsActivity.class);
                startActivity(intent);

            }
        });
        final TrailStation editStationObj = (TrailStation) getIntent().getSerializableExtra("stationName");
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
            stationId=editStationObj.getStationId();
            trailCode = editStationObj.getTrailCode();
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

                if (TextUtils.isEmpty(stationName)) {
                    Toast.makeText(CreateTrailStationActivity.this, "You must enter the Station Name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(instructions)) {
                    Toast.makeText(CreateTrailStationActivity.this, "You must enter Instructions for the Station", Toast.LENGTH_LONG).show();
                    return;
                }


                if (!editStation) {
                    try {
                        toolbar.setTitle("Create Trail Station for" +trailStationObj.getTrailCode());
                        createStation(trailStationObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    toolbar.setTitle("Update Trail Station for " +trailStationObj.getTrailCode());
                    try {
                        updateTrailStation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
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
        }else
        {stationId= trailStation.getStationId();}

        Log.d("stationId", stationId.toString());
        if (!TextUtils.isEmpty(stationId.toString())) {
            try {
                dbUtil.addRecordForCollection("TrailStation", trailStation, stationId.toString());
                if (!editStation)
                    Toast.makeText(CreateTrailStationActivity.this, "Station Created Successfully", Toast.LENGTH_SHORT).show();
            }
            catch(TrailDaoException daoException)
            {
                throw new Exception("Error occurred in Create Station invoking addRecordForCollection ", daoException);
            }
        finish();
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

}
