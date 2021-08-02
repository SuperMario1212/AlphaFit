package com.giruba.huaweicourse.alphafit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TutorialActivity extends AppCompatActivity {

    CardView chestbtn;
    CardView armsbtn;
    CardView legsbtn;
    CardView absbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        chestbtn = (CardView) findViewById(R.id.chestbtn);
        armsbtn = (CardView) findViewById(R.id.armsbtn);
        legsbtn = (CardView) findViewById(R.id.legbtn);
        absbtn = (CardView) findViewById(R.id.absbtn);

        chestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, ChestTutorial.class);
                startActivity(intent);
            }
        });

        armsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, ArmTutorial.class);
                startActivity(intent);
            }
        });


        legsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, LegTutorial.class);
                startActivity(intent);
            }
        });

        absbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, AbsTutorial.class);
                startActivity(intent);
            }
        });


    }
}