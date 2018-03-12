package edu.nus.trailblazelearn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.interfaces.LongClickListener;
import edu.nus.trailblazelearn.model.LearningTrail;

/**
 * Created by RMukherjee on 07-03-2018.
 */

public class LearningTrailAdapter extends RecyclerView.Adapter<LearningTrailAdapter.ViewHolder> {

    private static final String TAG = "LearningTrailAdapter";
    private List<LearningTrail> lstLearningTrail;
    private Context mContext;


    public LearningTrailAdapter(List<LearningTrail> lstLearningTrail, Context mContext) {
        this.lstLearningTrail = lstLearningTrail;
        this.mContext = mContext;
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    //List<LearningTrail> learningTrailLst = trailHelper.fetchTrailListForTrainer();
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,LongClickListener {

        public TextView mTextCode;
        public TextView mTextView;
        LongClickListener longClickListener;


        public ViewHolder(View v) {
            super(v);
            mTextCode = v.findViewById(R.id.tv_trail_code);
            mTextView = v.findViewById(R.id.tv_trail_name);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            this.longClickListener.onItemLongClick(getLayoutPosition());
            return false;

        }

        public void setLongClickListener(LongClickListener lcObj){
            this.longClickListener = lcObj;
        }


        @Override
        public void onItemLongClick(int position) {
            //v.setOnCreateContextMenuListener(this);
        }
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
        final int currentPosition = position;
        holder.mTextCode.setText(lstLearningTrail.get(position).getTrailCode());
        holder.mTextView.setText(lstLearningTrail.get(position).getTrailName());

        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                final LearningTrail trailObjData =lstLearningTrail.get(currentPosition);
                Toast.makeText(mContext,trailObjData.getTrailCode(),Toast.LENGTH_SHORT).show();

            }
        });
        final LearningTrail trailObjData =lstLearningTrail.get(currentPosition);
    }

    /**
     * API to remove Learning trail object on
     * long press event
     * @param trailObjData
     */
    private void removeLearningTrail(LearningTrail trailObjData) {
        int currPosition =  lstLearningTrail.indexOf(trailObjData);
        lstLearningTrail.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
