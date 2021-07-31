package com.giruba.huaweicourse.alphafit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QRScan extends AppCompatActivity {

    CardView camerascan;
    CardView imagescan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        camerascan = findViewById(R.id.camera_scan);
        imagescan = findViewById(R.id.img_scan);

        //scan with camera
        camerascan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRScan.this, CameraScan.class);
                startActivity(intent);
            }
        });

        //scan gallery image
        imagescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRScan.this, ImageScan.class);
                startActivity(intent);
            }
        });
    }
}