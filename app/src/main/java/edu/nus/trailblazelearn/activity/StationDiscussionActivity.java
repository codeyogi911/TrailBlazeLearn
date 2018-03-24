package edu.nus.trailblazelearn.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.UserProfileActivity;
import edu.nus.trailblazelearn.adapter.PostsListAdapter;
import edu.nus.trailblazelearn.helper.PostHelper;
import edu.nus.trailblazelearn.model.Post;
import edu.nus.trailblazelearn.model.TrailStation;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;

public class StationDiscussionActivity extends AppCompatActivity {


    private static final String TAG = "StationDiscussionAct";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    final String userName = (String) User.getData().get("name");
    //    User user = User.getInstance();
    boolean isTrainer = (boolean) User.getData().get("isTrainer");
    /**
     * EditText type object
     */
    private EditText inputMessageBox;
    /**
     * An image button object
     */
    private ImageButton postButton, takePhotoButton;
    private List<Post> postList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView postsRecyclerView;
    private FirebaseFirestore db;
    private TrailStation trailStation;
    private String trailCode;
    private String stationName, resultMessgae, imageDownloaduri;
    private Uri imageUri;
    private StorageReference storageReference;
    private String userNameDb;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userNameDb = (String) User.getData().get("name");
            if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
                imageUri = data.getData();
                Bundle bundle = data.getExtras();
                Bitmap bmp = (Bitmap) bundle.get("data");
                imageUri = getImageUri(getApplicationContext(), bmp);
                resultMessgae = storeImageFileInDb(imageUri, userName, imageUri.getLastPathSegment());
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Toolbar messageListToolbar = findViewById(R.id.message_list_toolbar);
        setSupportActionBar(messageListToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        trailStation = (TrailStation) getIntent().getSerializableExtra(ApplicationConstants.trailStation);
        getSupportActionBar().setTitle("Discussion - " + trailStation.getStationId());

        if (trailStation != null) {
            trailCode = trailStation.getTrailCode();
            stationName = trailStation.getTrailStationName();
        }

        inputMessageBox = findViewById(R.id.edittext_chatbox);
        postButton = findViewById(R.id.sendButton);
        takePhotoButton = findViewById(R.id.takePhotoButton);

        postsRecyclerView = findViewById(R.id.layout_recyclerview);
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

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(StationDiscussionActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        else
        {
            onClickListeners();
        }

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
                            postsRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                        }

                        Log.d(TAG, "Current post list size for trainer: "+postList.size());
                    }
                });
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
        // [END listen_multiple]

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

    public void onIconSelect(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(),
                UserProfileActivity.class);
        startActivity(intent);
    }

    private void onClickListeners() {
        try {
            takePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(startCamera, 1);
                }
            });

        }
        catch (Exception e)
        {
            Log.e(TAG,"There was an error while adding your post");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickListeners();
            } else {
                // Permission Denied
                Toast.makeText(StationDiscussionActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
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

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("Image uri :", path);
        return Uri.parse(path);
    }

    private String storeImageFileInDb(Uri uri, String userName, String child) {
        storageReference = FirebaseStorage.getInstance().getReference("activity").child(userName).child(child);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                resultMessgae = "success";
                imageDownloaduri = taskSnapshot.getDownloadUrl().toString();
                    Post postObj = new Post();
                    postObj.setImageUri(imageDownloaduri);
                    postObj.setUserName(userNameDb);
                    postObj.setStationID(stationName);
                    postObj.setTrailCode(trailCode);
                    new PostHelper().addPost(postObj);
                    Log.d(TAG, "Successfully added post to the db");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultMessgae = "failure";
                Toast.makeText(StationDiscussionActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        });
        return resultMessgae;
    }

}
