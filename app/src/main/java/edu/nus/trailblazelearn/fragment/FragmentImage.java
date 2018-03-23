package edu.nus.trailblazelearn.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.helper.RetriveImageBitmap;
import edu.nus.trailblazelearn.model.ParticipantItem;

@SuppressLint("ValidFragment")
public class FragmentImage extends Fragment {
    private ParticipantItem participantItem = new ParticipantItem();

    public FragmentImage(ParticipantItem participantItem) {
        this.participantItem = participantItem;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View fragmentImageView = inflater.inflate(R.layout.fragment_image, container, false);
        TextView notFoundImage = fragmentImageView.findViewById(R.id.image_files_not_found);
        for (int i = 0; i < participantItem.getVideoUri().size(); i++) {
            notFoundImage.setVisibility(View.GONE);
            final String imageuri = participantItem.getImageUri().get(i);
            GridLayout imageGridLayout = fragmentImageView.findViewById(R.id.fragment_image_grid_layout);
            ImageView imageView = new ImageView(fragmentImageView.getContext());
            GridLayout.LayoutParams lpImage = new GridLayout.LayoutParams();
            lpImage.width = 400;
            lpImage.height = 400;
            lpImage.setMargins(10, 10, 10, 10);
            imageView.setLayoutParams(lpImage);
            RetriveImageBitmap retriveImageBitmap = new RetriveImageBitmap(fragmentImageView.getContext(), imageView);
            retriveImageBitmap.execute(imageuri);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(imageuri), "image/*");
                    startActivity(intent);
                }
            });
            imageGridLayout.addView(imageView);
        }
            return fragmentImageView;
    }
}
