package com.android.yutaoren.searchapp;

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
                SearchTab searchTab = new SearchTab();
                return searchTab;

            case 1:
                FavoritesTab favoritesTab = new FavoritesTab();
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
