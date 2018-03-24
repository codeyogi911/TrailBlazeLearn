package edu.nus.trailblazelearn.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.activity.ParticipantContributedItemsActivity;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.User;



public class ParticipantItemAdapter extends RecyclerView.Adapter<ParticipantItemAdapter.ViewHolder> {
    private static Context context;
    private ViewHolder viewHolder;
    //    private User user = User.getInstance();
    private String userEmail = (String) User.getData().get("email");
    private ProgressBar progressBar;
    private ArrayList<ParticipantItem> participantItemArrayList = new ArrayList<>();

    public ParticipantItemAdapter(Context context, ArrayList<ParticipantItem> participantItemArrayList) {
        ParticipantItemAdapter.context = context;
        this.participantItemArrayList = participantItemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.participant_item, parent, false);
        //viewHolder = new ViewHolder(itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.participantName.setText(participantItemArrayList.get(position).getUserId());
        if (participantItemArrayList.get(position).getDescription().length() > 100) {
            holder.participantDescription.setText("   " + participantItemArrayList.get(position).getDescription().substring(0, 100) + " ...");
        } else {
            holder.participantDescription.setText("   " + participantItemArrayList.get(position).getDescription());
        }
        if (participantItemArrayList.get(position).getImageUri() != null) {
            if (participantItemArrayList.get(position).getImageUri().size() == 0)
                holder.imageIcon.setVisibility(View.INVISIBLE);
        }
        if (participantItemArrayList.get(position).getVideoUri() != null) {
            if (participantItemArrayList.get(position).getVideoUri().size() == 0)
                holder.videoIcon.setVisibility(View.INVISIBLE);
        }
        if (participantItemArrayList.get(position).getAudioUri() != null) {
            if (participantItemArrayList.get(position).getAudioUri().size() == 0)
                holder.audioIcon.setVisibility(View.INVISIBLE);
        }
        if (participantItemArrayList.get(position).getFileUri() != null) {
            if (participantItemArrayList.get(position).getFileUri().size() == 0)
                holder.fileIcon.setVisibility(View.INVISIBLE);
        }


        holder.participantItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = holder.getAdapterPosition();
                Intent intent = new Intent(context, ParticipantContributedItemsActivity.class);
                Log.d("ADAPTER_LOG", participantItemArrayList.get(itemPosition).getImageUri().get(0));

                intent.putExtra("participantItems", participantItemArrayList.get(itemPosition));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return participantItemArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView participantName;
        TextView participantDescription;
        CardView participantItem;
        ImageView imageIcon;
        ImageView videoIcon;
        ImageView audioIcon;
        ImageView fileIcon;

        public ViewHolder(final View itemView) {
            super(itemView);

            participantName = itemView.findViewById(R.id.participant_name);
            participantDescription = itemView.findViewById(R.id.participant_description);
            participantItem = itemView.findViewById(R.id.participant_item);
            imageIcon = itemView.findViewById(R.id.image_icon);
            videoIcon = itemView.findViewById(R.id.video_icon);
            audioIcon = itemView.findViewById(R.id.audio_icon);
            fileIcon = itemView.findViewById(R.id.document_icon);

        }

    }

}
