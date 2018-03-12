package edu.nus.trailblazelearn.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.helper.AddParticipantItemHelper;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.UploadedFiles;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.dbUtil;

public class ParticipantAddItemActivity extends AppCompatActivity {
    Button chooseImage, chooseAudio, chooseVideo, chooseDocument, createActivity;
    TextView selectedImageName, selectedAudioName, selectedVideoName, selectedFileName;
    AddParticipantItemHelper addParticipantItemHelper;
    EditText imageDescription;
    VideoView videoView;
    ImageView imageView;
    ProgressBar addItemProgressbar;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_AUDIO = 2;
    private static final int RESULT_LOAD_DOCUMENT = 4;
    private static final int RESULT_LOAD_VIDEO = 3;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int RESULT_LOAD_IMAGE_CAPTURE = 5;
    private static final int RESULT_LOAD_VIDEO_CAPTURE = 6;
    private HashMap<String, Uri> uriHashMap = new HashMap<>();
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    Uri selectedFile;
    String imageURL = null;
    String[] documentTypes = {"text/*", "application/pdf", "application/msword"};
    String[] addImageItems = {"camera", "gallary"};
    String[] addVideoItems = {"Record", "Gallary"};
    private User user;
    private String userEmail;
    private UploadedFiles uploadedFiles;
    ArrayList<String> fileUri = null;
    private ArrayList<String> fileType;
    private ParticipantItem participantItem;

    //String[] imageTypes = {"image/png"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_add_item);
        user = User.getInstance();
        userEmail = (String) user.getData().get("email");

        /*Toolbar addActivityToonbar = findViewById(R.id.add_activity);
        addActivityToonbar.setTitle("ADD ACTIVITY");*/

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        chooseImage = findViewById(R.id.choose_image);
        chooseAudio = findViewById(R.id.choose_audio);
        chooseVideo = findViewById(R.id.choose_video);
        chooseDocument = findViewById(R.id.choose_document);
        selectedImageName = findViewById(R.id.selected_image);
        selectedAudioName = findViewById(R.id.selected_audio);
        selectedVideoName = findViewById(R.id.selected_video);
        selectedFileName = findViewById(R.id.selected_doc);
        createActivity = findViewById(R.id.upload_files);
        imageDescription = findViewById(R.id.image_description);
        addItemProgressbar = findViewById(R.id.add_item_progress);
        addItemProgressbar.setVisibility(View.INVISIBLE);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            onclickListeners();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File name;
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK && data != null) && (requestCode == 1 || requestCode == 2 || requestCode == 4 || requestCode == 5 || requestCode == 3 || requestCode == 6)) {
            //if(requestCode != RESULT_LOAD_IMAGE_CAPTURE) {
            Log.d("request code", Integer.toString(requestCode));
                selectedFile = data.getData();

            //}
            if (requestCode == RESULT_LOAD_IMAGE) {
                imageURL = getDataColumn(getApplicationContext(), selectedFile, null, null);
                name = new File(imageURL);
                uriArrayList.add(selectedFile);
                uriHashMap.put("image", selectedFile);
                dialogUpload(selectedFile, RESULT_LOAD_IMAGE, name.getName());
            }
            else if(requestCode == RESULT_LOAD_IMAGE_CAPTURE) {
                Bundle bundle = data.getExtras();
                Bitmap bmp = (Bitmap) bundle.get("data");
                selectedFile = getImageUri(getApplicationContext(), bmp);
                imageURL = getDataColumn(getApplicationContext(), selectedFile, null, null);
                name = new File(imageURL);
                uriArrayList.add(selectedFile);
                uriHashMap.put("image", selectedFile);
                dialogUpload(selectedFile, RESULT_LOAD_IMAGE, name.getName());
            }
            else if(requestCode == RESULT_LOAD_VIDEO || requestCode == RESULT_LOAD_VIDEO_CAPTURE) {
                imageURL = getDataColumn(getApplicationContext(), selectedFile, null, null);
                name = new File(imageURL);
                uriArrayList.add(selectedFile);
                uriHashMap.put("video", selectedFile);
                dialogUpload(selectedFile, RESULT_LOAD_VIDEO, name.getName());
            }
            else if (requestCode == RESULT_LOAD_AUDIO) {
                imageURL = getDataColumn(getApplicationContext(), selectedFile, null, null);
                name = new File(imageURL);
                uriArrayList.add(selectedFile);
                uriHashMap.put("audio", selectedFile);
                dialogUpload(selectedFile, RESULT_LOAD_AUDIO, name.getName());
            } else if (requestCode == RESULT_LOAD_DOCUMENT) {
                String id = DocumentsContract.getDocumentId(selectedFile);
                /*final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://"), Long.valueOf(id));*/
                imageURL = getDataColumn(getApplicationContext(), selectedFile, null, null);
                name = new File(imageURL);
                uriArrayList.add(selectedFile);
                uriHashMap.put("document", selectedFile);
                dialogUpload(selectedFile, RESULT_LOAD_DOCUMENT, name.getName());
            }
            //Log.d("Image path and doc path", selectedImage.getPath().toString() + " " + name.getName() + " " + imageURL);
            //Toast.makeText(ParticipantAddItemActivity.this, name.getName() + " " + selectedImage.getEncodedPath().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogUpload(Uri uri, final int code, final String name) {
        View dialogView = null;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ParticipantAddItemActivity.this);
        dialogBuilder.setTitle("Confirm it BudDy");
        //final ImageView imageView = dialogView.findViewById(R.id.dialog_upload_image);
        //final TextView textView = dialogView.findViewById(R.id.dialog_upload_text);
        //textView.setText(name);
        /*if(code == RESULT_LOAD_IMAGE) {
            imageView.setImageURI(uri);
        }*/
        if(code == RESULT_LOAD_VIDEO || code == RESULT_LOAD_VIDEO_CAPTURE) {
        MediaController mediaController = new MediaController(ParticipantAddItemActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.upload_video, null);
        videoView = dialogView.findViewById(R.id.dialog_upload_video);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();

        }
        if(code == RESULT_LOAD_IMAGE || code == RESULT_LOAD_IMAGE_CAPTURE) {
            dialogView = getLayoutInflater().inflate(R.layout.upload_image, null);
            imageView = dialogView.findViewById(R.id.upload_image);
            imageView.setImageURI(uri);
            selectedImageName.setText(name);
        }
        Button uploadButton = dialogView.findViewById(R.id.upload_action);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParticipantItemHelper = new AddParticipantItemHelper(getApplicationContext(), uriHashMap, userEmail, addItemProgressbar);
                addParticipantItemHelper.execute(uriArrayList);
                alertDialog.dismiss();
                addItemProgressbar.setVisibility(View.VISIBLE);
                if(code == RESULT_LOAD_VIDEO || code == RESULT_LOAD_VIDEO_CAPTURE) {
                    selectedVideoName.setText(name);
                }
                if(code == RESULT_LOAD_IMAGE || code == RESULT_LOAD_IMAGE_CAPTURE) {
                    selectedImageName.setText(name);
                }
                if(code == RESULT_LOAD_AUDIO) {
                    selectedAudioName.setText(name);
                }
                if(code == RESULT_LOAD_DOCUMENT) {
                    selectedFileName.setText(name);
                }
                uriHashMap.clear();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private void onclickListeners() {
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ParticipantAddItemActivity.this);
                dialogBuilder.setTitle("Add Image");
                dialogBuilder.setItems(addImageItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(addImageItems[which].equals("camera")) {
                            Log.d("camera", "addImageItems");
                            Intent cameraAction = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraAction, RESULT_LOAD_IMAGE_CAPTURE);
                        }
                        else {
                            Log.d("gallary", "select image");
                            Intent chooseImageFromGallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            chooseImageFromGallary.setType("image/*");
                            chooseImageFromGallary.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, 3);
                            startActivityForResult(Intent.createChooser(chooseImageFromGallary, "select image"), RESULT_LOAD_IMAGE);
                        }
                    }
                });
                dialogBuilder.show();
            }
        });

        chooseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseAudioFromGallary = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(chooseAudioFromGallary, RESULT_LOAD_AUDIO);
            }
        });

        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ParticipantAddItemActivity.this);
                dialogBuilder.setTitle("Add Video");
                dialogBuilder.setItems(addVideoItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(addVideoItems[which].equals("Record")) {
                            Log.d("Record", "addImageItems");
                            Intent cameraAction = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            startActivityForResult(cameraAction, RESULT_LOAD_VIDEO_CAPTURE);
                        }
                        else {
                            Log.d("gallery", "select image");
                            Intent chooseImageFromGallary = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            chooseImageFromGallary.setType("video/*");
                            chooseImageFromGallary.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, 3);
                            startActivityForResult(Intent.createChooser(chooseImageFromGallary, "select Video"), RESULT_LOAD_VIDEO);
                        }
                    }
                });
                dialogBuilder.show();
            }
        });

        chooseDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/*").putExtra(Intent.EXTRA_MIME_TYPES, documentTypes);
                startActivityForResult(intent, RESULT_LOAD_DOCUMENT);
            }
        });

        createActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(imageDescription.getText())) {
                    //Code to create contributor item
                    //fileUri = UploadedFiles.downLoadUri;
                    participantItem = new ParticipantItem(userEmail, "trailId", 1234, imageDescription.getText().toString());
                    if(dbUtil.imageUriList != null) {
                        participantItem.setImageUri(dbUtil.imageUriList);
                    }
                    if(dbUtil.videoUriList != null) {
                        participantItem.setVideoUri(dbUtil.videoUriList);
                    }
                    if(dbUtil.audioUriList != null) {
                        participantItem.setAudioUri(dbUtil.audioUriList);
                    }
                    if(dbUtil.documentUriList != null) {
                        participantItem.setFileUrl(dbUtil.documentUriList);
                    }

                    dbUtil.addObjectToDB("participantActivities", participantItem);
                    finish();
                }
                else {
                    if(TextUtils.isEmpty(imageDescription.getText())) {
                        imageDescription.setError("Description required..");
                    }
                    else {
                    Toast.makeText(ParticipantAddItemActivity.this, "Please select an Image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onclickListeners();
            } else {
                // Permission Denied
                Toast.makeText(ParticipantAddItemActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("Image uri :", path);
        Toast.makeText(ParticipantAddItemActivity.this, path, Toast.LENGTH_LONG).show();
        return Uri.parse(path);
    }
}

