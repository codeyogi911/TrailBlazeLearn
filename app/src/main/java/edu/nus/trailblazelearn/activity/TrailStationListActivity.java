package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.TrailStationListAdapter;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.utility.ApplicationConstants;

public class TrailStationListActivity extends AppCompatActivity {

    private static final String TAG = ApplicationConstants.trailStaionListActivity;
    Toolbar toolBarStationListActivity;
    ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private TrailStationListAdapter trailStationAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private List<TrailStation> trailStationList;
    private FirebaseFirestore firebaseFirestore;
    private String trailCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Start of onCreate Station List Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_station_list);
        toolBarStationListActivity = findViewById(R.id.StationListHeader);
        toolBarStationListActivity.setTitle("Trail Stations");
        recyclerView = (RecyclerView) findViewById(R.id.StationRecyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        trailStationList = new ArrayList<TrailStation>();

        // use a linear layout manager
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        //Set Adapter
        trailStationAdapter = new TrailStationListAdapter(trailStationList, this);
        recyclerView.setAdapter(trailStationAdapter);

        //Register context menu with list item
        registerForContextMenu(recyclerView);
        LearningTrail trailObj = (LearningTrail) getIntent().getSerializableExtra(ApplicationConstants.trailCode);
        trailCode= trailObj.getTrailCode();


        //Set mFireStore to call Firebase Collection

        firebaseFirestore.collection("TrailStation")
                .whereEqualTo("trailCode", trailCode)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, ApplicationConstants.errorDbSelectionForCollectionFailed, e);
                            Toast.makeText(TrailStationListActivity.this, ApplicationConstants.toastMessageForDbFailure, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            TrailStation trailStationObj = doc.getDocument().toObject(TrailStation.class);
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                trailStationList.add(trailStationObj);
                            } else if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                int editedPosition = trailStationAdapter.itemPosition;
                                Log.d(TAG, "Edited position :: " + trailStationAdapter.itemPosition);
                                Log.d(TAG, "Station Name ::" + trailStationObj.getTrailStationName());
                                trailStationList.set(editedPosition, trailStationObj);
                            }

                            trailStationAdapter.notifyDataSetChanged();
                        }

                        Log.d(TAG, "Current learning trail list size for trainer: " + trailStationList.size());
                    }
                });


        Log.d(TAG, "End of onCreate API call");

    }

    /**
     * Create Station
     */
    public void CreateTrailStation(View v) {
        Log.d(TAG, "Start CreateTrailStation");
        Intent intent = new Intent(getApplicationContext(), CreateTrailStationActivity.class);
        intent.putExtra("trailCode",trailCode);
        startActivity(intent);
        Log.d(TAG, "End Create Trail Station");
    }

    /**
     * menu for Edit and Delete option
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(TAG, "Start of onCreateContextMenu API call");
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context_menu, menu);
        Log.d(TAG, "End of onCreateContextMenu API call");

    }

    /**
     * API to remove or edit on click of respective context menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "Start of onContextItemSelected API call");
        super.onContextItemSelected(item);
        final AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = trailStationAdapter.selectedItemPosition();

        switch (item.getItemId()) {

            case R.id.edit_menu_item:
                TrailStation stationObj = new TrailStation();
                stationObj = trailStationList.get(position);
                Intent intent = new Intent(this, CreateTrailStationActivity.class);
                intent.putExtra("stationName", stationObj);
                startActivity(intent);
                trailStationAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete_menu_item:
                Log.d(TAG, "Station name.." + trailStationList.get(position).getTrailStationName());
                firebaseFirestore.collection("TrailStation").document("/" + trailStationList.get(position).getTrailStationName())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                trailStationAdapter.removeTrailStation(trailStationList.get(position));
                                Log.d(TAG, "Station Deleted" + trailStationList.size());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error deleting trail station", e);
                            }
                        });
                return true;
            default:
                Log.d(TAG, "End of onContextItemSelected API call");
                return super.onContextItemSelected(item);
        }

    }

}
