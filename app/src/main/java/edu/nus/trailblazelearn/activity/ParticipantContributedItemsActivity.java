package edu.nus.trailblazelearn.activity;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.PagerAdapter;
import edu.nus.trailblazelearn.helper.RetriveImageBitmap;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.User;

public class ParticipantContributedItemsActivity extends AppCompatActivity {
private ParticipantItem participantItem = new ParticipantItem();
private RetriveImageBitmap retriveImageBitmap;
User user = User.getInstance(this);
public boolean isTrainer = (boolean) user.getData().get("isTrainer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_contributed_items);
        RelativeLayout relativeLayout = findViewById(R.id.participant_item_relative_layout);
        TextView participantName = findViewById(R.id.participant_in_item_name);
        TextView participantDescription = findViewById(R.id.participant_in_item_description);
        Toolbar toolbar = findViewById(R.id.tb_participant_contributed_list_header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ViewPager viewPager = findViewById(R.id.pager);
        participantItem = (ParticipantItem) getIntent().getSerializableExtra("participantItems");

        getSupportActionBar().setTitle(" Activity Details" + participantItem.getTrailStationId());
        if(isTrainer) {
            getSupportActionBar().setIcon(R.drawable.icons_trainer);
        }
        else {
            getSupportActionBar().setIcon(R.drawable.icons_student);
        }

        participantName.setText(participantItem.getUserId());
        participantDescription.setText(participantItem.getDescription());
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

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }
}
