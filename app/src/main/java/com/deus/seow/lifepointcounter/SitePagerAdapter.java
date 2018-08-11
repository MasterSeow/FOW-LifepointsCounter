package com.deus.seow.lifepointcounter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SitePagerAdapter extends FragmentPagerAdapter {

    Fragment lifeFragment = new LifeFragment();
    Fragment historyFragment = new HistoryFragment();
    Fragment counterFragment = new CounterFragment();

    SitePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return historyFragment;
            case 1:
                return lifeFragment;
            case 2:
                return counterFragment;
        }
        return new LifeFragment();
    }


    @Override
    public int getCount() {
        return 3;
    }

}
