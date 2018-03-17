package edu.nus.trailblazelearn;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FragmentImage extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {


        View fragmentImageView = inflater.inflate(R.layout.fragment_image, container, false);
        LinearLayout imageLinearLayout = fragmentImageView.findViewById(R.id.fragment_image_linear_layout);
        ImageView imageView = new ImageView(fragmentImageView.getContext());
        LinearLayout.LayoutParams lpImage=new LinearLayout.LayoutParams(350, 350);
        imageView.setLayoutParams(lpImage);
        
        return fragmentImageView;
    }
}
