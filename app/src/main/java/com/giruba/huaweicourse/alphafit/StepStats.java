package com.giruba.huaweicourse.alphafit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepStats extends AppCompatActivity implements SensorEventListener {

    @BindView(R.id.piechart)
    PieChart mPieChart;

    @BindView(R.id.total)
    TextView totalView;

    @BindView(R.id.average)
    TextView averager;

    @BindView(R.id.barChart)
    BarChart mBarChart;

    PieModel sliceCurrent,sliceGoal;

    @BindView(R.id.steps)
    TextView steps;

    @BindView(R.id.unit)
    TextView tvUnit;
    private boolean running = true;

    private int todayOffset, total_start, goal, since_boot, total_days;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());

    SensorManager sensorManager;

    private static final int DEFAULT_STEPS = 10000;
    final static float DEFAULT_STEP_SIZE = Locale.getDefault() == Locale.US ? 2.5f : 75f;
    final static String DEFAULT_STEP_UNIT = Locale.getDefault() == Locale.US ? "ft" : "cm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_stats);

        ButterKnife.bind(this);

        // green slice: current steps which is already taken
        sliceCurrent = new PieModel("", 0, Color.parseColor("#99CC00"));
        mPieChart.addPieSlice(sliceCurrent);
        // red slice: steps which haven't take to reach goal(6k steps)
        sliceGoal = new PieModel("", DEFAULT_STEPS, Color.parseColor("#CC0000"));
        mPieChart.addPieSlice(sliceGoal);
        mPieChart.setDrawValueInPie(false);
        mPieChart.setUsePieRotation(true);

        mPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                running = !running;
                stepsDistanceChanged();
            }
        });

        mPieChart.startAnimation();

        //Bar chart
//        mBarChart.addBar(new BarModel(2.3f, 0xFF123456));
//        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
//        mBarChart.addBar(new BarModel(3.3f, 0xFF563456));
//        mBarChart.addBar(new BarModel(1.1f, 0xFF873F56));
//        mBarChart.addBar(new BarModel(2.7f, 0xFF56B7F1));
//        mBarChart.addBar(new BarModel(2.f,  0xFF343456));
//        mBarChart.addBar(new BarModel(0.4f, 0xFF1FF4AC));
//        mBarChart.addBar(new BarModel(4.f,  0xFF1BA4E6));
//        mBarChart.startAnimation();



    }

    @Override
    protected void onResume() {
        super.onResume();

        Database db = Database.getInstance(this);

        // todayoffset gets today steps from DB
        todayOffset = db.getSteps(DayUtils.getToday());

        SharedPreferences prefs =
                this.getSharedPreferences("pedometer", Context.MODE_PRIVATE);

        //goal = 6000 steps
        goal = prefs.getInt("goal", DEFAULT_STEPS);
        //everytime boot , get current steps
        since_boot = db.getCurrentSteps();
        int pauseDifference = since_boot - prefs.getInt("pauseCount", since_boot);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //sensor checker
        if(sensor != null){
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
        }else {
            Toast.makeText(this,"Sensor not found", Toast.LENGTH_SHORT).show();
        }
        since_boot -= pauseDifference;

        total_start = db.getTotalWithoutToday();
        total_days = db.getDays();
        db.close();
        stepsDistanceChanged();

    }

    //step distance display
    private void stepsDistanceChanged() {
        //if functioning, then continue below steps
        if (running) {
            tvUnit.setText("Steps");
        } else {
            String unit = this.getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getString("stepsize_unit",DEFAULT_STEP_UNIT);
            if (unit.equals("cm")) {
                unit = "km";
            } else {
                unit = "mi";
            }
            tvUnit.setText(unit);
        }

        updatePie();
        updateBars();
    }


    //pause, running =  false
    //step sensor stop sensing
    //current steps saved in DB
    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        try {
            SensorManager sm =
                    (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
        }

        //current steps saved in DB
        Database db = Database.getInstance(this);
        db.saveCurrentSteps(since_boot);
        db.close();
    }


    // not understood
    @Override
    public void onSensorChanged(SensorEvent event) {
        //current values override previous values
        if (event.values[0] > Integer.MAX_VALUE || event.values[0] == 0) {
            return;
        }
        // todayoffset is today steps from DB
        //not understood from here
        if (todayOffset == Integer.MIN_VALUE) {
            todayOffset = -(int) event.values[0];
            Database db = Database.getInstance(this);
            db.insertNewDay(DayUtils.getToday(), (int) event.values[0]);
            db.close();
        }
        since_boot = (int) event.values[0];
        Log.e("STEPS",todayOffset+"");
        updatePie();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //199 225 67 106 75
    }

    private void updatePie() {
        int steps_today = Math.max(todayOffset+since_boot,0);
        sliceCurrent.setValue(steps_today);

        //goal =  6k steps
        //if not reach goal
        if (goal - steps_today > 0) {
            if (mPieChart.getData().size() == 1) {
                mPieChart.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(goal - steps_today);
        } else {
            //if reach goal, clear chart and add pie slice
            mPieChart.clearChart();
            mPieChart.addPieSlice(sliceCurrent);
        }
        mPieChart.update();

        //visualize daily steps in pie analysis chart
        if (running) {
            steps.setText(formatter.format(steps_today));
            totalView.setText(formatter.format(total_start + steps_today));
            averager.setText(formatter.format((total_start + steps_today) / total_days));
        } else {
            //some algorithms to calculate steps
            // update only every 10 steps when displaying distance
            SharedPreferences prefs =
                    this.getSharedPreferences("pedometer", Context.MODE_PRIVATE);
            float stepsize = prefs.getFloat("stepsize_value", DEFAULT_STEP_SIZE);
            float distance_today = steps_today * stepsize;
            float distance_total = (total_start + steps_today) * stepsize;
            if (prefs.getString("stepsize_unit", DEFAULT_STEP_UNIT)
                    .equals("cm")) {
                distance_today /= 100000;
                distance_total /= 100000;
            } else {
                distance_today /= 5280;
                distance_total /= 5280;
            }

            //interface display steps, total distance, average distance
            steps.setText(formatter.format(distance_today));
            totalView.setText(formatter.format(distance_total));
            averager.setText(formatter.format(distance_total / total_days));
        }
    }


    private void updateBars() {
        SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());
        if (mBarChart.getData().size() > 0) mBarChart.clearChart();
        int steps;
        float distance, stepsize = DEFAULT_STEP_SIZE;
        boolean stepsize_cm = true;
        if (!running) {
            // load some more settings if distance is needed
            SharedPreferences prefs =
                    this.getSharedPreferences("pedometer", Context.MODE_PRIVATE);
            stepsize = prefs.getFloat("stepsize_value",DEFAULT_STEP_SIZE);
            stepsize_cm = prefs.getString("stepsize_unit",DEFAULT_STEP_UNIT)
                    .equals("cm");
        }
        mBarChart.setShowDecimal(!running); // show decimal in distance view only
        BarModel bm;
        Database db = Database.getInstance(this);
        List<Pair<Long, Integer>> last = db.getLastEntries(8);
        db.close();
        for (int i = last.size() - 1; i > 0; i--) {
            Pair<Long, Integer> current = last.get(i);
            steps = current.second;
            if (steps > 0) {
                bm = new BarModel(df.format(new Date(current.first)), 0,
                        steps > goal ? Color.parseColor("#99CC00") : Color.parseColor("#0099cc"));
                if (running) {
                    bm.setValue(steps);
                } else {
                    distance = steps * stepsize;
                    if (stepsize_cm) {
                        distance /= 100000;
                    } else {
                        distance /= 5280;
                    }
                    distance = Math.round(distance * 1000) / 1000f; // 3 decimals
                    bm.setValue(distance);
                }
                mBarChart.addBar(bm);
            }
        }
        //only show bar stats when and only if data value exist (greater than 0)
        if (mBarChart.getData().size() > 0) {
            mBarChart.startAnimation();
        } else {
            mBarChart.setVisibility(View.GONE);
        }
    }
}