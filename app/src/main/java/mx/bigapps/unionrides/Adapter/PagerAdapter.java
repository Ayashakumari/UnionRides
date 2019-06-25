package mx.bigapps.unionrides.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mx.bigapps.unionrides.Activity.Recent_Chat;
import mx.bigapps.unionrides.Fragment.Clubs;
import mx.bigapps.unionrides.Fragment.Fans;
import mx.bigapps.unionrides.Fragment.Page3;

/**
 * Created by admin on 01-11-2017.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fans one = new Fans();

                return one;
            case 1:
                Clubs two = new Clubs();
                return two;
            case 2:
                Page3 three = new Page3();
                return three;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}