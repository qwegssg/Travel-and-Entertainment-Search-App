package com.android.yutaoren.placesearchapp;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity  implements searchTab.OnFragmentInteractionListener,favoritesTab.OnFragmentInteractionListener{

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

//        for (int i = 0; i < tabLayout.getTabCount(); i++ ) {
//            TextView tabContent = (TextView) (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//            tabContent.setText("SEARCH");
//            tabContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon, 0, 0, 0);
//            tabLayout.getTabAt(i).setCustomView(tabContent);
//        }

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.search_icon).setText("SEARCH"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITES").setIcon(R.drawable.heart_fill_white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager searchPager = (ViewPager) findViewById(R.id.searchPager);
        final SearchPagerAdapter searchPagerAdpter = new SearchPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        searchPager.setAdapter(searchPagerAdpter);
        searchPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
