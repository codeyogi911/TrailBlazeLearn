package edu.nus.trailblazelearn.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.dbUtil;

/**
 * Created by dpak1 on 3/10/2018.
 */

public class ParticipantItemAdapter extends RecyclerView.Adapter<ParticipantItemAdapter.ViewHolder> {
    private Context context;
    private ViewHolder viewHolder;
    private User user = User.getInstance();
    private String userEmail = (String) user.getData().get("email");
    private ProgressBar progressBar;
    private ArrayList<ParticipantItem> participantItemArrayList = new ArrayList<>();

    public ParticipantItemAdapter(Context context, ArrayList<ParticipantItem> participantItemArrayList) {
        this.context = context;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.participantName.setText(participantItemArrayList.get(position).getUserId());
        if(participantItemArrayList.get(position).getDescription().length() > 100) {
            holder.participantDescription.setText("   "+participantItemArrayList.get(position).getDescription().substring(0, 100) + " ...");
        }
        else {
            holder.participantDescription.setText("   "+participantItemArrayList.get(position).getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return participantItemArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView participantName;
        TextView participantDescription;
        CardView participantItem;

        public ViewHolder(View itemView) {
            super(itemView);
            participantName = itemView.findViewById(R.id.participant_name);
            participantDescription = itemView.findViewById(R.id.participant_description);
        }
    }

}
