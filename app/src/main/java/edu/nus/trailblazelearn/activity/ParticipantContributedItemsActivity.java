package edu.nus.trailblazelearn.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.PagerAdapter;
import edu.nus.trailblazelearn.helper.RetriveImageBitmap;
import edu.nus.trailblazelearn.model.ParticipantItem;

public class ParticipantContributedItemsActivity extends AppCompatActivity {
private ParticipantItem participantItem = new ParticipantItem();
private RetriveImageBitmap retriveImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_contributed_items);
        RelativeLayout relativeLayout = findViewById(R.id.participant_item_relative_layout);
        final ViewPager viewPager = findViewById(R.id.pager);
        participantItem = (ParticipantItem) getIntent().getSerializableExtra("participantItems");
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), participantItem, 4);
        viewPager.setAdapter(pagerAdapter);
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Images"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Audios"));
        tabLayout.addTab(tabLayout.newTab().setText("Document"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                               @Override
                                               public void onTabSelected(TabLayout.Tab tab) {
                                                   viewPager.setCurrentItem(tab.getPosition());
                                               }

                                               @Override
                                               public void onTabUnselected(TabLayout.Tab tab) {

                                               }

                                               @Override
                                               public void onTabReselected(TabLayout.Tab tab) {

                                               }
                                           });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
}
