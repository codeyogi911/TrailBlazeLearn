package edu.nus.trailblazelearn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.UserProfileActivity;
import edu.nus.trailblazelearn.adapter.LearningTrailAdapter;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DbUtil;

public class LearningTrailListActivity extends AppCompatActivity implements ApplicationConstants {

    private static final String TAG = ApplicationConstants.trailActivityListClassName;
    public static boolean isTrainer;
    Toolbar toolBarListLearningActivity;
    ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private LearningTrailAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<LearningTrail> learningTrailLst;
    private FirebaseFirestore mFireStore;
    private User user;
    private String userEmail;
    private FloatingActionButton createTrail;
    private TextView defaultTrailMessage;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.participant_default, menu);
        MenuItem participant = menu.findItem(R.id.participant_icon);
        participant.setVisible(false);
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
        User.signOut(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Start of onCreate API call");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_trail_list);
//        user = User.getInstance();
        userEmail = (String) User.getData().get(ApplicationConstants.email);
        isTrainer = (boolean) User.getData().get("isTrainer");

        toolBarListLearningActivity = findViewById(R.id.tb_trail_list_header);
        setSupportActionBar(toolBarListLearningActivity);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.page_heading_learning_list));

        mRecyclerView = findViewById(R.id.learning_trail_recycler_view);
        createTrail = findViewById(R.id.fab_create_trail);
        mProgressBar = findViewById(R.id.pb_trail_list);
        defaultTrailMessage = findViewById(R.id.learning_trail_not_found);
        mFireStore = FirebaseFirestore.getInstance();



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        learningTrailLst = new ArrayList<LearningTrail>();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Set Adapter
        mAdapter = new LearningTrailAdapter(learningTrailLst, this);
        mRecyclerView.setAdapter(mAdapter);

        //Register context menu with list item
        registerForContextMenu(mRecyclerView);

        createTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForCreateTrail();
            }
        });

        //Set mFireStore to call Firebase Collection
        // [START listen_multiple]

        mFireStore.collection(ApplicationConstants.learningTrailCollection)
                .whereEqualTo(ApplicationConstants.userId, userEmail)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, ApplicationConstants.errorDbSelectionForCollectionFailed, e);
                            Toast.makeText(LearningTrailListActivity.this, ApplicationConstants.toastMessageForDbFailure, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            LearningTrail learningTrailObj = doc.getDocument().toObject(LearningTrail.class);
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                learningTrailLst.add(learningTrailObj);
                            } else if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                int editedPosition = mAdapter.currentPosition;
                                Log.d(TAG, "Edited position :: " + mAdapter.currentPosition);
                                Log.d(TAG, "Learning code ::" + learningTrailObj.getTrailDescription());
                                learningTrailLst.set(editedPosition, learningTrailObj);
                            }

                            mAdapter.notifyDataSetChanged();
                            if (learningTrailLst.size() > 0) {
                                defaultTrailMessage.setVisibility(View.INVISIBLE);
                            } else {
                                defaultTrailMessage.setVisibility(View.VISIBLE);
                            }
                        }

                        Log.d(TAG, "Current learning trail list size for trainer: " + learningTrailLst.size());
                    }
                });
        // [END listen_multiple]
        mProgressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG, "End of onCreate API call");

    }

    /**
     * API to redirect to Create
     * Learning Trail Activity Page
     * @param
     */
    public void startActivityForCreateTrail(){
        Log.d(TAG, "Start of startActivityForCreateTrail API call");
        Intent intent = new Intent(getApplicationContext(), CreateLearningTrailActivity.class);
        startActivity(intent);
        Log.d(TAG, "End of startActivityForCreateTrail API call");
    }

    /**
     * API to create and inflate
     * menu for Edit and Delete option
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        Log.d(TAG, "Start of onCreateContextMenu API call");
        super.onCreateContextMenu(menu,v,menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context_menu,menu);
        Log.d(TAG, "End of onCreateContextMenu API call");

    }

    /**
     * API to remove or edit on click of respective context menu
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        Log.d(TAG, "Start of onContextItemSelected API call");
        super.onContextItemSelected(item);
        final AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = mAdapter.selectedItemPosition();

        switch(item.getItemId()){

            case R.id.edit_menu_item :
                LearningTrail trailObj = new LearningTrail();
                trailObj = learningTrailLst.get(position);
                Intent intentObj = new Intent(this, CreateLearningTrailActivity.class);
                intentObj.putExtra(ApplicationConstants.trailCode, trailObj);
                startActivity(intentObj);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete_menu_item :
                String trailCodeForDeletion = learningTrailLst.get(position).getTrailCode();
                //Invoke api to delete enrolled trail for trailCode
                try {
                    DbUtil.deleteTrail(trailCodeForDeletion);
                } catch (Exception e) {
                    Log.e(TAG, "Exception occurred while deleting enrolled trail");
                    Toast.makeText(LearningTrailListActivity.this, ApplicationConstants.toastMessageForDbFailure, Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "Trail code to be deleted.." + learningTrailLst.get(position).getTrailCode());
                mAdapter.removeLearningTrail(learningTrailLst.get(position));
                                Log.d(TAG, "Learning Trail successfully deleted...size is!" + learningTrailLst.size());
                return true;
            default:
                Log.d(TAG, "End of onContextItemSelected API call");
                return super.onContextItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
}
