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

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.exception.TrailDaoException;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.dbUtil;


public class CreateTrailStationActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "sg.edu.nus.trailstation.MESSAGE ";
    private static final String TAG = "CreateTrailStation";
    public static FirebaseFirestore firebaseStorage;
    private static StorageReference storageReference;
    public EditText stationName, instructions, txtKeyword, stationId;
    private TextView txtDetails;
    private Button btnSave, btnSearch;
    private String trailCode;
    private boolean editStation;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trail_station);
        toolbar = (Toolbar) findViewById(R.id.tb_trail_header);
        setSupportActionBar(toolbar);

        //Get the trailCode from Intent passed
      //  final TrailStation trailStationObj = (TrailStation) getIntent().getSerializableExtra(ApplicationConstants.trailCodeParam);
        trailCode =(String) getIntent().getSerializableExtra(ApplicationConstants.trailCode);
        //trailCode = trailStationObj.getTrailCode();

        //txtDetails = (TextView) findViewById(R.id.text1);
        stationName = (EditText) findViewById(R.id.station_name);
        instructions = (EditText) findViewById(R.id.station_instructions);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSearch = (Button) findViewById(R.id.btn_search);

        firebaseStorage = firebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnSearch.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(CreateTrailStationActivity.this, MapsActivity.class);
                //String message= txtKeyword.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE,message);
                startActivity(intent);

            }
        });

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String stationName = ((EditText) findViewById(R.id.station_name)).getText().toString();
                String instructions = ((EditText) findViewById(R.id.station_instructions)).getText().toString();
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
                final TrailStation editStationObj = (TrailStation) getIntent().getSerializableExtra(stationName);

                if (!editStation) {
                    toolbar.setTitle("Create Trail Station");
                    createStation(trailStationObj);
                } else {
                    updateTrailStation();
                }


                if (editStation) {
                    TrailStation editLearningStationObj = new TrailStation();
                    Log.d(TAG, "Edit Trail Station for" + editStationObj.getTrailCode());
                    toolbar.setTitle("Edit Trail Station");
                    //   stationName.setText(editLearningStationObj.getTrailStationName());
                    // stationName.setEnabled(false);
                    //instructions.setText(editLearningStationObj.getStationInstructions());
                    trailCode = editLearningStationObj.getTrailCode();

                }
            }
        });

    }

    /**
     * Creating new Trail Station node under 'Learning Trail'
     */
    private void createStation(TrailStation trailStation) {
        // TODO
        if (!TextUtils.isEmpty(trailCode)) {
                dbUtil.addObjectToDB("TrailStation", trailStation);
            Toast.makeText(CreateTrailStationActivity.this, "Station Created Successfully", Toast.LENGTH_SHORT).show();
        finish();
        }
    }

    private void updateTrailStation() {
        Log.d(TAG, "Start updateTrailStation ");
        TrailStation updateStationObj = showStationDetails();
        /**Construct Helper to call DB and persist data**/
        createStation(updateStationObj);
        Toast.makeText(CreateTrailStationActivity.this, "Updated Succesfully",
                Toast.LENGTH_SHORT).show();

    }

    private TrailStation showStationDetails() {
        Log.d(TAG, "display of created station details");
        TrailStation trailStationObj = new TrailStation();
        trailStationObj.setTrailStationName(stationName.getText().toString());
        trailStationObj.setStationInstructions(instructions.getText().toString());
        if (trailCode != null) {
            trailStationObj.setTrailCode(trailCode);
        }
        return trailStationObj;
    }

}
