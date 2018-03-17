package edu.nus.trailblazelearn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.nus.trailblazelearn.fragment.FragmentAudio;
import edu.nus.trailblazelearn.fragment.FragmentDocument;
import edu.nus.trailblazelearn.fragment.FragmentImage;
import edu.nus.trailblazelearn.fragment.FragmentVideo;
import edu.nus.trailblazelearn.model.ParticipantItem;

/**
 * Created by dpak1 on 3/15/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int itemCount;
    private ParticipantItem participantItem = new ParticipantItem();
    public PagerAdapter(FragmentManager fragmentManager, ParticipantItem participantItem, int itemCount) {
        super(fragmentManager);
        this.itemCount = itemCount;
        this.participantItem = participantItem;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentImage fragmentImage = new FragmentImage(participantItem);
                return fragmentImage;
            case 1:
                FragmentVideo fragmentVideo = new FragmentVideo(participantItem);
                return fragmentVideo;
            case 2:
                FragmentAudio fragmentAudio = new FragmentAudio(participantItem);
                return fragmentAudio;
            case 3:
                FragmentDocument fragmentDocument = new FragmentDocument();
                return fragmentDocument;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return itemCount;
    }
}
