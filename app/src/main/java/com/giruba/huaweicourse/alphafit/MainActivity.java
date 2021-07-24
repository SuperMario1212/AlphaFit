package com.giruba.huaweicourse.alphafit;

//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView steps;
    private TextView distance;
    private TextView calories;

    Button playmusic;
    Button tutorials;
    Button checklist;
    Button bodytrans;
    Button qrscan;
    Button searchgym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //these use toast text
        steps = findViewById(R.id.steptxt);
        distance = findViewById(R.id.distancetxt);
        calories = findViewById(R.id.caloriestxt);

        //these needs to direct to new activity
        playmusic = (Button) findViewById(R.id.musicbtn);
        tutorials = (Button) findViewById(R.id.tutorialsbtn);
        checklist = (Button) findViewById(R.id.trainingchecklistbtn);
        bodytrans = (Button) findViewById(R.id.bodytransformationbtn);
        qrscan = (Button) findViewById(R.id.qrbtn);
        searchgym = (Button) findViewById(R.id.gymlocationbtn);

        tutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
                startActivity(intent);
            }
        });


        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckList.class);
                startActivity(intent);
            }
        });
    }
}
