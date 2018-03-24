package edu.nus.trailblazelearn.adapter;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.activity.TrailStationListActivity;
import edu.nus.trailblazelearn.model.LearningTrail;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<DocumentSnapshot> enrolledTrails = new ArrayList();
    private AppCompatActivity appCompatActivity;

    public RecyclerAdapter(Object object, List<DocumentSnapshot> list) {
        appCompatActivity = (AppCompatActivity) object;
        enrolledTrails = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText((String) enrolledTrails.get(i).get("trailName"));
        viewHolder.itemDetail.setText((String) enrolledTrails.get(i).get("trailDescription"));
        viewHolder.doc = enrolledTrails.get(i);
    }

    @Override
    public int getItemCount() {
        return enrolledTrails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public int currentItem;
        //        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public DocumentSnapshot doc;

        public ViewHolder(View itemView) {
            super(itemView);
//            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDetail = itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(appCompatActivity.getApplicationContext(), TrailStationListActivity.class);
                    intent.putExtra("trailCode", doc.toObject(LearningTrail.class));
                    appCompatActivity.startActivity(intent);
//                    Snackbar.make(v, "Click detected on item " + position,
//                            Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                }
            });
        }
    }
}
