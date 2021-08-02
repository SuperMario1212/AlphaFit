package com.giruba.huaweicourse.alphafit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String WORKOUT_TABLE = "WORKOUT_TABLE";
    public static final String COLUMN_WORKOUT_NAME = "WORKOUT_NAME";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context,"workout.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + WORKOUT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WORKOUT_NAME + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //add a data
    public boolean addOne(WorkoutTypes workout){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_WORKOUT_NAME, workout.getWorkout());
        long insert = db.insert(WORKOUT_TABLE, null, cv);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    //delete a data
    public boolean DeleteOne(WorkoutTypes workout){
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + WORKOUT_TABLE +" WHERE " + COLUMN_ID + " = " + workout.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    public List<WorkoutTypes> getEveryOne(){
        List<WorkoutTypes> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + WORKOUT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int workoutID = cursor.getInt(0);
                String workoutName = cursor.getString(1);

                WorkoutTypes workout = new WorkoutTypes(workoutID, workoutName);
                returnList.add(workout);
            }while(cursor.moveToNext());
        }else{
            //
        }
        cursor.close();
        db.close();
        return returnList;

    }
}