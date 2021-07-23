package com.giruba.huaweicourse.alphafit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

// Ignore this class this is just for testing, and this class will not be used...


public class SwipeViewActivity extends AppCompatActivity {

    //actionbar
    private ActionBar actionBar;


    //UI Views
    private ViewPager viewPager;

    private ArrayList<MyModel> modelArrayList;
    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view);

        //init actionbar
        actionBar = getSupportActionBar();

        //init UI Views
        viewPager = findViewById(R.id.viewPager);
        loadCards();

        //  set viewpager change listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // it will just change the title of actionbar
                String title = modelArrayList.get(position).getTitle();
                actionBar.setTitle(title);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void loadCards() {
        //init list
        modelArrayList = new ArrayList<>();

        //add items to list
        modelArrayList.add(new MyModel(
                "Your lie in April",
                "Description 02",
                "02/08/2020",
                "https://youtu.be/IeJTNN8_jLI",
                R.drawable.kaori));

        modelArrayList.add(new MyModel(
                "Blend S",
                "Description",
                "1/1/2021",
                "https://youtu.be/4NnYhatt6eQ",
                R.drawable.kaho));

        modelArrayList.add(new MyModel(
                "download_no_equipment",
                "Description 02",
                "02/08/2020",
                "https://youtu.be/aL8kJ2nmqVQ",
                R.drawable.no_equipment_chest));

        modelArrayList.add(new MyModel(
                "men_link",
                "Description",
                "1/1/2021",
                "https://youtu.be/qM1nv8qcsN4",
                R.drawable.men_link_chest));

        modelArrayList.add(new MyModel("Women Chest Link",
                "women_link",
                "02/08/2020",
                "https://youtu.be/1n5H4KwB_ZU",
                R.drawable.women_link_chest));

        //  setup adapter
        myAdapter = new MyAdapter(this, modelArrayList);
        // set adapter to view pager
        viewPager.setAdapter(myAdapter);
        // set default padding
        viewPager.setPadding(100, 0, 100, 0);
    }
}