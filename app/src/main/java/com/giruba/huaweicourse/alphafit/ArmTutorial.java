package com.giruba.huaweicourse.alphafit;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ArmTutorial extends AppCompatActivity {

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
                "No Equipment for Arm",
                "Description 02",
                "10 mins",
                "https://youtu.be/wwKb-wZCEjs",
                R.drawable.no_equipment_arm));

        modelArrayList.add(new MyModel(
                "Men Link for Arm",
                "Description",
                "8 mins",
                "https://youtu.be/LHBINS08Yy0",
                R.drawable.men_link_arm));

        modelArrayList.add(new MyModel(
                "Women Link for Arm",
                "women_link",
                "12 mins",
                "https://youtu.be/l0CwCvJbGZI",
                R.drawable.women_link_arm));

        //  setup adapter
        myAdapter = new MyAdapter(this, modelArrayList);
        // set adapter to view pager
        viewPager.setAdapter(myAdapter);
        // set default padding
        viewPager.setPadding(100, 0, 100, 0);
    }
}
