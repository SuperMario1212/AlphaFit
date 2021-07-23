package com.giruba.huaweicourse.alphafit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorialActivity extends AppCompatActivity {

    Button arms;
    Button leg;
    Button abs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Button chest = (Button) findViewById(R.id.chestbtn);
        Button arms = (Button) findViewById(R.id.amrsbtn);
        Button leg = (Button) findViewById(R.id.legbtn);
        Button abs = (Button) findViewById(R.id.absbtn);

        chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, ChestTutorial.class);
                startActivity(intent);
            }
        });


        arms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, ArmTutorial.class);
                startActivity(intent);
            }
        });


        leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, LegTutorial.class);
                startActivity(intent);
            }
        });

        abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialActivity.this, AbsTutorial.class);
                startActivity(intent);
            }
        });

    }
}