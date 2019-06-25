package mx.bigapps.unionrides.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mx.bigapps.unionrides.Fragment.Awards;
import mx.bigapps.unionrides.Fragment.RidesPhotos;
import mx.bigapps.unionrides.Fragment.SpecRides;
import mx.bigapps.unionrides.Fragment.VideoRides;

/**
 * Created by admin on 01-11-2017.
 */
public class RidesPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public RidesPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RidesPhotos one = new RidesPhotos();
                return one;
            case 1:
                VideoRides two = new VideoRides();
                return two;
            case 2:
                SpecRides three = new SpecRides();
                return three;
            case 3:
                Awards four = new Awards();
                return four;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}