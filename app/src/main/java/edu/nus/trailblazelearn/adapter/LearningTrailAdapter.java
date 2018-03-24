package edu.nus.trailblazelearn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.activity.TrailStationListActivity;
import edu.nus.trailblazelearn.interfaces.LongClickListener;
import edu.nus.trailblazelearn.model.LearningTrail;


public class LearningTrailAdapter extends RecyclerView.Adapter<LearningTrailAdapter.ViewHolder> {


    private static final String TAG = "LearningTrailAdapter";
    public int currentPosition;
    private List<LearningTrail> lstLearningTrail;
    private Context mContext;



    public LearningTrailAdapter(List<LearningTrail> lstLearningTrail, Context mContext) {
        this.lstLearningTrail = lstLearningTrail;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return lstLearningTrail.size();
    }

    @Override
    public LearningTrailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Create a new view.
        View viewObj = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.learning_trail_item, viewGroup, false);

        ViewHolder viewHolderObj = new  ViewHolder(viewObj);
        return viewHolderObj;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextCode.setText(lstLearningTrail.get(position).getTrailCode());
        holder.mTextView.setText(lstLearningTrail.get(position).getTrailName());

        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                currentPosition = position;
                final LearningTrail trailObjData = lstLearningTrail.get(position);
                Toast.makeText(mContext, trailObjData.getTrailCode(), Toast.LENGTH_SHORT).show();

            }

        });
    }

    /**
     * API to remove Learning trail object on
     * long press event
     * @param trailObjData
     */
    public void removeLearningTrail(LearningTrail trailObjData) {
        int currPosition =  lstLearningTrail.indexOf(trailObjData);
        lstLearningTrail.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public int selectedItemPosition() {
        return currentPosition;

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    //List<LearningTrail> learningTrailLst = trailHelper.fetchTrailListForTrainer();
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, LongClickListener,OnClickListener {

        public TextView mTextCode;
        public TextView mTextView;
        LongClickListener longClickListener;


        public ViewHolder(View v) {
            super(v);
            mTextCode = v.findViewById(R.id.tv_trail_code);
            mTextView = v.findViewById(R.id.tv_trail_name);
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
            currentPosition = position;
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG,"Start of onClick method");
            int position = getLayoutPosition();
            LearningTrail trailObj = new LearningTrail();
            trailObj = lstLearningTrail.get(position);
            Intent intentObj = new Intent(mContext, TrailStationListActivity.class);
            intentObj.putExtra("trailCode", trailObj);
            Log.i(TAG,"trailCode::"+trailObj.getTrailCode());
            mContext.startActivity(intentObj);
            Log.i(TAG,"End of onClick method");
        }
    }

}
