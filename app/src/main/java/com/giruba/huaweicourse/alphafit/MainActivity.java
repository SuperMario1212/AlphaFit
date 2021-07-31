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
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.service.AccountAuthService;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {



    CardView playmusic;
    CardView tutorials;
    CardView checklist;
    CardView qrscan;
    CardView stepstats;
    CardView signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //these needs to direct to new activity
        playmusic = (CardView)findViewById(R.id.musicbtn);
        tutorials = (CardView)findViewById(R.id.tutorialsbtn);
        checklist = (CardView)findViewById(R.id.trainingchecklistbtn);
        qrscan = (CardView)findViewById(R.id.qrbtn);
        stepstats = (CardView)findViewById(R.id.step_stats);
        signOut = (CardView) findViewById(R.id.signout);


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


        //play music
        //ListMusicActivity.class
        playmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListMusicActivity.class);
                startActivity(intent);
            }
        });


        qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QRScan.class);
                startActivity(intent);
            }
        });

        stepstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StepStats.class);
                startActivity(intent);
            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams();
                AccountAuthService service = AccountAuthManager.getService(MainActivity.this, authParams);
                // service indicates the AccountAuthService instance generated using the getService method during the sign-in authorization.
                service.cancelAuthorization().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Processing after a successful authorization revoking.
                            Log.i(TAG, "onSuccess: ");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish(); //    This is mainly used to stop the user from pressing back button to bypass the login and back to the main menu.
                        } else {
                            // Handle the exception.
                            Exception exception = task.getException();
                            if (exception instanceof ApiException){
                                int statusCode = ((ApiException) exception).getStatusCode();
                                Log.i(TAG, "onFailure: " + statusCode);
                            }
                        }
                    }
                });
            }
        });


    }
}