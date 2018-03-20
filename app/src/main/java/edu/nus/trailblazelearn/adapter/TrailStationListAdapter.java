package edu.nus.trailblazelearn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.activity.CreateTrailStationActivity;
import edu.nus.trailblazelearn.interfaces.LongClickListener;
import edu.nus.trailblazelearn.model.TrailStation;

/**
 * Created by Dharini.
 */

public class TrailStationListAdapter extends RecyclerView.Adapter<TrailStationListAdapter.ViewHolder> {
    private static final String TAG = "TrailStationAdaper";
    public int itemPosition;
    private List<TrailStation> trailStationList;
    private Context context;

    public TrailStationListAdapter(List<TrailStation> trailStationList, Context context) {
        this.trailStationList = trailStationList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return trailStationList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Create a new view.
        View viewObj = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trail_station_list, viewGroup, false);

        ViewHolder viewHolderObj = new ViewHolder(viewObj);
        return viewHolderObj;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(trailStationList.get(position).getTrailStationName());

        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                itemPosition = position;
                final TrailStation trailStationObj = trailStationList.get(itemPosition);
                Toast.makeText(context, trailStationObj.getTrailCode(), Toast.LENGTH_SHORT).show();

            }

        });
    }

    /**
     * API to remove Learning trail object on
     * long press event
     *
     * @param trailStationObj
     */
    public void removeTrailStation(TrailStation trailStationObj) {
        int currPosition = trailStationList.indexOf(trailStationObj);
        trailStationList.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public int selectedItemPosition() {
        return itemPosition;

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    //List<LearningTrail> learningTrailLst = trailHelper.fetchTrailListForTrainer();
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, LongClickListener, View.OnClickListener {

        public TextView textCode;
        public TextView textView;
        LongClickListener longClickListener;


        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.StationName);
            v.setOnLongClickListener(this);
            v.setOnClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            this.longClickListener.onItemLongClick(getLayoutPosition());
            return false;

        }

        public void setLongClickListener(LongClickListener lcObj) {
            this.longClickListener = lcObj;
        }


        @Override
        public void onItemLongClick(int position) {
            itemPosition = position;
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Start of onClick method");
            int position = getLayoutPosition();
            TrailStation trailStationObj = new TrailStation();
            trailStationObj = trailStationList.get(position);
            Intent intent = new Intent(context, CreateTrailStationActivity.class);
            intent.putExtra("trailCode", trailStationObj);
            Log.i(TAG, "stationName:" + trailStationObj.getTrailCode());
            context.startActivity(intent);
            Log.i(TAG, "End of onClick method");
        }
    }
}
