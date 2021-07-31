package com.giruba.huaweicourse.alphafit;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class AbsTutorial extends AppCompatActivity {

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
        loadCards1();

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


    private void loadCards1() {
        //init list
        modelArrayList = new ArrayList<>();

        //add items to list

        modelArrayList.add(new MyModel(
                "No Equipment for Abs",
                "learn to build abs without equipment!",
                "10 mins",
                "https://youtu.be/R8oZk2wnpY0",
                R.drawable.no_equipment_abs));

        modelArrayList.add(new MyModel(
                "Men Abs Exercises",
                "Build abs if you have dumbbells, weights equipments etc",
                "7 mins",
                "https://youtu.be/MoIZR4p_Q5w",
                R.drawable.men_link_abs));

        modelArrayList.add(new MyModel(
                "Women Abs Exercises",
                "Abs workout for ladies !",
                "10 mins",
                "https://youtu.be/AnYl6Nk9GOA",
                R.drawable.women_link_abs));

        //  setup adapter
        myAdapter = new MyAdapter(this, modelArrayList);
        // set adapter to view pager
        viewPager.setAdapter(myAdapter);
        // set default padding
        viewPager.setPadding(100, 0, 100, 0);
    }
}
