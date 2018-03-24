package edu.nus.trailblazelearn.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.activity.ParticipantAddItemActivity;


public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> audioListUri;

    public AudioListAdapter(Context context, ArrayList<String> audioListUri) {
        this.context = context;
        this.audioListUri = audioListUri;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View audioView = layoutInflater.inflate(R.layout.audio_list, parent, false);
        return new ViewHolder(audioView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.audioName.setText("Audio file : " + ++position);

        holder.audioList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = holder.getAdapterPosition();
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(audioListUri.get(itemPosition)), "audio/*");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioListUri.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
    TextView audioName;
    CardView audioList;
        public ViewHolder(View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audio_name_in_list);
            audioList = itemView.findViewById(R.id.audi_list_card_view);
        }
    }
}
