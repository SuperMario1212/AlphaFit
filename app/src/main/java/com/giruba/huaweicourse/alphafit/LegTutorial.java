package com.giruba.huaweicourse.alphafit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;

public class LegTutorial extends AppCompatActivity {

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
                "No Equipment For Leg",
                "Description 02",
                "8 mins",
                "https://youtu.be/UKM_3T2-Huc",
                R.drawable.no_equipment_leg));

        modelArrayList.add(new MyModel(
                "Men Link for Leg",
                "Description",
                "8 mins",
                "https://youtu.be/eFtbf720CQY",
                R.drawable.men_link_leg));

        modelArrayList.add(new MyModel("Women Chest Link",
                "Women Link for Leg",
                "13 mins",
                "https://youtu.be/Fu_oExrPX68",
                R.drawable.women_link_leg));

        //  setup adapter
        myAdapter = new MyAdapter(this, modelArrayList);
        // set adapter to view pager
        viewPager.setAdapter(myAdapter);
        // set default padding
        viewPager.setPadding(100, 0, 100, 0);
    }
}