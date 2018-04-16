package com.android.yutaoren.placesearchapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public SearchPagerAdapter(FragmentManager fm, int mNoOfTabs) {
        super(fm);
        this.mNoOfTabs = mNoOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {

            case 0:
                searchTab searchTab = new searchTab();
                return searchTab;

            case 1:
                favoritesTab favoritesTab = new favoritesTab();
                return favoritesTab;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}

