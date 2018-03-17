package edu.nus.trailblazelearn.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.AudioListAdapter;
import edu.nus.trailblazelearn.model.ParticipantItem;


@SuppressLint("ValidFragment")
public class FragmentAudio extends Fragment {
private ParticipantItem participantItem = new ParticipantItem();
AudioListAdapter audioListAdapter;
//private static String audioUri;
    public FragmentAudio(ParticipantItem participantItem) {
        this.participantItem = participantItem;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentAudioView = inflater.inflate(R.layout.fragment_audio, container, false);
        LinearLayout audioLinearLayout = fragmentAudioView.findViewById(R.id.fragment_audio_linear_layout);

        RecyclerView recyclerView = fragmentAudioView.findViewById(R.id.audio_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentAudioView.getContext()));
        audioListAdapter = new AudioListAdapter(fragmentAudioView.getContext(), participantItem.getAudioUri());
        recyclerView.setAdapter(audioListAdapter);
        //audioLinearLayout.addView(recyclerView);

        return fragmentAudioView;
    }

}
