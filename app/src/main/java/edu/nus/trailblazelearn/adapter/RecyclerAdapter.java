package edu.nus.trailblazelearn.adapter;
/**
 * Created by Shashwat on 9/3/2018.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.activity.TrailStationListActivity;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.User;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> enrolledTrails = new ArrayList<>();
    private AppCompatActivity appCompatActivity;
//    private DocumentSnapshot documentSnapshot;
//    private String trailCode;

//    private int[] images = { R.drawable.android_image_1,
//            R.drawable.android_image_2,
//            R.drawable.android_image_3,
//            R.drawable.android_image_4,
//            R.drawable.android_image_5,
//            R.drawable.android_image_6,
//            R.drawable.android_image_7,
//            R.drawable.android_image_8 };

    public RecyclerAdapter(Object object) {
        appCompatActivity = (AppCompatActivity) object;
        final List<String> t = (List<String>) User.getInstance().getData().get("enrolledTrails");
        List<String> df = new ArrayList<>();
        df.add("Default");
        enrolledTrails = t == null ? df : t;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
//        ViewHolder viewHolder = new ViewHolder(v);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        FirebaseFirestore.getInstance().collection("LearningTrail").document(enrolledTrails.get(i)).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        documentSnapshot=documentSnapshot1;
                        if (documentSnapshot.exists()) {
                            viewHolder.itemTitle.setText((String) documentSnapshot.get("trailName"));
                            viewHolder.itemDetail.setText((String) documentSnapshot.get("trailDescription"));
                            viewHolder.doc = documentSnapshot;
                        }
                    }
                }
        );

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
