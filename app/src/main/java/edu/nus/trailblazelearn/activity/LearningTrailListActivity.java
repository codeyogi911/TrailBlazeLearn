package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.LearningTrailAdapter;
import edu.nus.trailblazelearn.helper.LearningTrailHelper;
import edu.nus.trailblazelearn.model.LearningTrail;

public class LearningTrailListActivity extends AppCompatActivity {

    private static final String TAG = "LearningTrailListActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolBarListLearningActivity;
    ProgressBar mProgressBar;
    private List<LearningTrail> learningTrailLst;
    private FirebaseFirestore mFireStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_trail_list);
        toolBarListLearningActivity = findViewById(R.id.tb_trail_list_header);
        toolBarListLearningActivity.setTitle(getString(R.string.page_heading_learning_list));
        mRecyclerView = (RecyclerView) findViewById(R.id.learning_trail_recycler_view);
        mProgressBar = findViewById(R.id.pb_trail_list);
        mFireStore = FirebaseFirestore.getInstance();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        learningTrailLst = new ArrayList<LearningTrail>();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Set Adapter
        mAdapter = new LearningTrailAdapter(learningTrailLst,this);
        mRecyclerView.setAdapter(mAdapter);

        //Register context menu with list item
        registerForContextMenu(mRecyclerView);



        //Set mFireStore to call Firebase Collection
        // [START listen_multiple]
        mFireStore.collection("LearningTrail")
                .whereEqualTo("userId","ms.romila@gmail.com" )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }


                        Log.w(TAG,"Query snapshot :"+value.getQuery());

                        for (DocumentChange doc : value.getDocumentChanges()) {

                            LearningTrail learningTrailObj = doc.getDocument().toObject(LearningTrail.class);
                            learningTrailLst.add(learningTrailObj);
                            mAdapter.notifyDataSetChanged();
                        }

                        Log.d(TAG, "Current learning trail list size for trainer: "+learningTrailLst.size());
                    }
                });
        // [END listen_multiple]


        mProgressBar.setVisibility(View.INVISIBLE);

    }

    /**
     * API to redirect to Create
     * Learning Trail Activity Page
     * @param v
     */
    public void startActivityForCreateTrail(View v){
        Intent intent = new Intent(getApplicationContext(), CreateLearningTrailActivity.class);
        startActivity(intent);
    }

    /**
     * API to create menu for Edit and Delete option
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context_menu,menu);
    }

    /**
     * API to remove or edit on context menu
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

       Log.d(TAG, "onContextTemSelected info selected: "+info.position);
        switch(item.getItemId()){

            case R.id.edit_menu_item :
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete_menu_item :
                learningTrailLst.remove(info.position);
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
