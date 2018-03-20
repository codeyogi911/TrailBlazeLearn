package edu.nus.trailblazelearn.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.PostsListAdapter;
import edu.nus.trailblazelearn.helper.PostHelper;
import edu.nus.trailblazelearn.model.Post;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;

public class StationDiscussionActivity extends AppCompatActivity {


    private static final String TAG = "StationDiscussionActivity";
    /**
     * EditText type object
     */
    private EditText inputMessageBox;
    /**
     * An image button object
     */
    private ImageButton postButton;


    private User user;
    private List<Post> postList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView postsRecyclerView;

    private FirebaseFirestore db;
    private TrailStation trailStation;
    private String trailCode;
    private String stationName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Toolbar messageListToolbar = findViewById(R.id.message_list_toolbar);
        setSupportActionBar(messageListToolbar);

        user = User.getInstance(getApplicationContext());
        final String userName = (String) user.getData().get("name");


        db = FirebaseFirestore.getInstance();
        trailStation = (TrailStation) getIntent().getSerializableExtra(ApplicationConstants.trailStation);

        if (trailStation != null) {
            trailCode = trailStation.getTrailCode();
            stationName = trailStation.getTrailStationName();
        }

        inputMessageBox = findViewById(R.id.edittext_chatbox);
        postButton = findViewById(R.id.sendButton);

        postsRecyclerView = (RecyclerView) findViewById(R.id.layout_recyclerview);
        postsRecyclerView.setHasFixedSize(true);
        postList = new ArrayList<Post>();


        // use a  layout manager
        mLayoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(mLayoutManager);

        //Set Adapter
        mAdapter = new PostsListAdapter(postList,this,userName);

        postsRecyclerView.setAdapter(mAdapter);

        //Register context menu with list item
        registerForContextMenu(postsRecyclerView);

        db.collection(ApplicationConstants.Post)
                .whereEqualTo(ApplicationConstants.stationID, stationName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed.", e);
                            return;
                        }
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            Post postObj= doc.getDocument().toObject(Post.class);
                            postList.add(postObj);
                            mAdapter.notifyDataSetChanged();
                        }

                        Log.d(TAG, "Current post list size for trainer: "+postList.size());
                    }
                });
        // [END listen_multiple]


            try {

                postButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (isValid()) {
                            Post postObj = new Post();
                            postObj.setMessage(inputMessageBox.getText().toString());
                            postObj.setUserName(userName);
                            postObj.setStationID(stationName);
                            postObj.setTrailCode(trailCode);
                            new PostHelper().addPost(postObj);
                            Log.d(TAG, "Successfully added post to the db");
                            inputMessageBox.getText().clear();
                        }
                    }

                });
            }
            catch (Exception e)
            {
                Log.e(TAG,"There was an error while adding your post");

            }



    }

    /**
     * Checks whether a text field is empty or not
     * @return boolean value that signifies whether a text field is empty
     */
    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(inputMessageBox.getText().toString().trim())) {
            inputMessageBox.setError(getString(R.string.valid_message_discussion));
            isValid = false;
        }
        return isValid;
    }



}
