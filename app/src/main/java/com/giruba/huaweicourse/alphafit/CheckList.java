package com.giruba.huaweicourse.alphafit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class CheckList extends AppCompatActivity {

    Button btn_add,stopStartButton;
    EditText et_workout;
    TextView timerText;

    boolean timerStarted = false;

    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;


    ArrayAdapter workoutArrayAdapter;
    DataBaseHelper dataBaseHelper;
    ListView lv_workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        btn_add = findViewById(R.id.btn_add);
        et_workout = findViewById(R.id.et_workout);
        lv_workoutList = findViewById(R.id.workout_list);
        timerText = findViewById(R.id.timerText);
        stopStartButton = findViewById(R.id.startStopButton);
        timer = new Timer();

        dataBaseHelper = new DataBaseHelper(CheckList.this);
        ShowWorkoutOnListView(dataBaseHelper);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkoutTypes workout;

                try{
                    workout = new WorkoutTypes(-1,et_workout.getText().toString());
                    et_workout.getText().clear();
                    Toast.makeText(CheckList.this, workout.getWorkout() + " added", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(CheckList.this, "Error adding workout item", Toast.LENGTH_SHORT).show();
                    workout = new WorkoutTypes(-1,"err");
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(CheckList.this);
                boolean success = dataBaseHelper.addOne(workout);
                ShowWorkoutOnListView(dataBaseHelper);

                Toast.makeText(CheckList.this, "Success", Toast.LENGTH_SHORT).show();

                ShowWorkoutOnListView(dataBaseHelper);

            }
        });

        lv_workoutList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WorkoutTypes clickedWorkouts = (WorkoutTypes) parent.getItemAtPosition(position);
                final int pos = position;

                new AlertDialog.Builder(CheckList.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete this ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.DeleteOne(clickedWorkouts);
                                ShowWorkoutOnListView(dataBaseHelper);
                                Toast.makeText(CheckList.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("no", null)
                        .show();
                return true;
            }
        });

    }

    public void resetTapped(View view){
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Reset timer?");
        resetAlert.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(timerTask != null)
                {
                    timerTask.cancel();
                    stopStartButton.setText("START");
                    time = 0.0;
                    timerStarted = false;
                    timerText.setText(formatTime(0,0,0));
                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //null
            }
        });
        resetAlert.show();
    }

    public void startStopTapped(View view){
        if(timerStarted == false)
        {
            timerStarted = true;
            stopStartButton.setText("STOP");


            startTimer();
        }else {
            timerStarted = false;
            stopStartButton.setText("START");

            timerTask.cancel();
        }
    }

    private void startTimer()
    {
        timerTask = new TimerTask() {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours){
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

    private void ShowWorkoutOnListView(DataBaseHelper dbhelper) {
        workoutArrayAdapter = new ArrayAdapter<WorkoutTypes>(CheckList.this, android.R.layout.simple_list_item_1, dbhelper.getEveryOne());
        lv_workoutList.setAdapter(workoutArrayAdapter);
    }
}