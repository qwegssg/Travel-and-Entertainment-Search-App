package com.android.yutaoren.searchapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SearchTab.OnFragmentInteractionListener,FavoritesTab.OnFragmentInteractionListener{

    private EditText keywordInput, otherLocInput;
    private TextView keywordValidation;
    private TextView otherLocVlidation;
    private Button searchBtn, clearBtn;
    private RadioButton currentLocBtn, otherLocBtn;

    private TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
//
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchPlaces();
//            }
//        });
//
////        if otherLocBtn is checked, enable the other location input field
//        otherLocBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked) {
//                    otherLocInput.setEnabled(true);
//                } else {
//                    otherLocInput.setEnabled(false);
//                }
//            }
//        });
////        if currentLocBtn is checked, hide the validation of other location input field
//        currentLocBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked) {
//                    otherLocVlidation.setVisibility(View.GONE);
//                }
//            }
//        });
//
////        when input has changed, hide the validation
//        keywordInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                keywordValidation.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                keywordValidation.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                keywordValidation.setVisibility(View.GONE);
//            }
//        });
//        otherLocInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                otherLocVlidation.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                otherLocVlidation.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                otherLocVlidation.setVisibility(View.GONE);
//            }
//        });
    }





    private void initWidgets() {
        keywordInput = (EditText) findViewById(R.id.keywordInput);
        otherLocInput = (EditText) findViewById(R.id.otherLocInput);
        otherLocInput.setEnabled(false);

        keywordValidation = (TextView) findViewById(R.id.keywordValidation);
        otherLocVlidation = (TextView) findViewById(R.id.otherLocValidation);

        currentLocBtn = (RadioButton) findViewById(R.id.currentLocBtn);
        otherLocBtn = (RadioButton) findViewById(R.id.otherLocBtn);

        searchBtn = (Button) findViewById(R.id.searchBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("SEARCH"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITES"));
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
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });

    }

    private void searchPlaces() {
        boolean isValid = true;

//        Trim removes first and last space of a string entered by a user.
//        if the keyword input is empty:
        if(keywordInput.getText().toString().trim().isEmpty() == true) {
            keywordValidation.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, R.string.validationToast, Toast.LENGTH_LONG).show();
            isValid = false;
        }
//        if otherLocBtn is checked and the other location input is empty:
        if(otherLocBtn.isChecked() == true) {
            if(otherLocInput.getText().toString().trim().isEmpty() == true) {
                otherLocVlidation.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, R.string.validationToast, Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
