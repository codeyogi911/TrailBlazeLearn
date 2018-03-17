package edu.nus.trailblazelearn.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.ParticipantItem;

import static android.view.SurfaceHolder.*;

@SuppressLint("ValidFragment")
public class FragmentVideo extends Fragment {

    private ParticipantItem participantItem = new ParticipantItem();
    MediaController mediaController;
    File file;
    private static String videoUri;

    public FragmentVideo(ParticipantItem participantItem) {
        this.participantItem = participantItem;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentVideoView = inflater.inflate(R.layout.fragment_video, container, false);
        GridLayout videoGridLayout = fragmentVideoView.findViewById(R.id.fragment_video_grid_layout);
        for(int i = 0; i<participantItem.getVideoUri().size();i++) {
            videoUri = participantItem.getVideoUri().get(i);
            VideoView videoView = new VideoView(fragmentVideoView.getContext());
            GridLayout.LayoutParams lpVideo = new GridLayout.LayoutParams();
            lpVideo.height = 400;
            lpVideo.width = 400;
            lpVideo.setMargins(10, 10, 10, 10);
            videoView.setLayoutParams(lpVideo);
            videoView.setVideoURI(Uri.parse(videoUri));
            videoView.requestFocus();
        /*mediaController = new MediaController(fragmentVideoView.getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);*/
            videoView.setZOrderOnTop(false);
            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(videoUri), "video/*");
                    startActivity(intent);
                    return false;
                }
            }); /*{
                @Override
                public void onClick(View v) {
                    //videoView.start();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setDataAndType(Uri.parse(videoUri), "video*//*");
                    startActivity(intent);
                }
            });*/
            videoView.seekTo(100);

            videoGridLayout.addView(videoView);
        }
        return fragmentVideoView;
    }
}

