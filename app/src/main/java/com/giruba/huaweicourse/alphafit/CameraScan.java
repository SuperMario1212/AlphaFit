package com.giruba.huaweicourse.alphafit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

public class CameraScan extends AppCompatActivity {

    public static final int DEFAULT_VIEW = 0x22;
    private static final int REQUEST_CODE_SCAN = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);
    }

    //check permissions for camera and read file
    public void newViewBtnClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    DEFAULT_VIEW);
        }
    }

    //start scan
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions == null || grantResults == null || grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (requestCode == DEFAULT_VIEW) {
            //start ScankitActivity for scanning barcode
            ScanUtil.startScan(CameraScan.this, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
        }
    }

    //result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Process the result after the scanning is complete.
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        // Use ScanUtil.RESULT as the key value to obtain the return value of HmsScan from data returned by the onActivityResult method.
        if (requestCode == REQUEST_CODE_SCAN) {
            Object obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    //scan result return here
                    Toast.makeText(this, ((HmsScan) obj).getOriginalValue(),
                            Toast.LENGTH_SHORT).show();
                    //this message shows actions
                    Intent i = new Intent(Intent.ACTION_VIEW);;
                    i.setData(Uri.parse((((HmsScan) obj).getOriginalValue()).toString()));
                    startActivity(i);
                }
                return;
            }
        }
    }
}