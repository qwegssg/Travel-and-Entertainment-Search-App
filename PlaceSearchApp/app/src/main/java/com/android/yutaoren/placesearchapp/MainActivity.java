package com.android.yutaoren.placesearchapp;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements searchTab.OnFragmentInteractionListener,favoritesTab.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
    }

    private void initWidgets() {

//        remove the shadow below the tool bar
        getSupportActionBar().setElevation(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        List<Integer> tabIcons = new ArrayList<>();
        tabIcons.add(R.drawable.search_icon);
        tabIcons.add(R.drawable.heart_fill_white);

        final ViewPager searchPager = (ViewPager) findViewById(R.id.searchPager);
        final SearchPagerAdapter searchPagerAdpter = new SearchPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        searchPager.setAdapter(searchPagerAdpter);

        for (int i = 0; i < tabLayout.getTabCount(); i++ ) {
            LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_search_tab, null);
            TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
            tabContent.setText(getResources().getStringArray(R.array.searchTabContent)[i]);
            tabContent.setTextColor(getResources().getColor(R.color.colorSelectedTabText));
            tabContent.setCompoundDrawablesWithIntrinsicBounds(tabIcons.get(i), 0, 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabContent);
        }

//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.search_icon).setText(" SEARCH"));
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.heart_fill_white).setText(" FAVORITES"));

//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        searchPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
