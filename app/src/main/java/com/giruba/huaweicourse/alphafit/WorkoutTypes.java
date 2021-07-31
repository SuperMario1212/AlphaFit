package com.giruba.huaweicourse.alphafit;

public class WorkoutTypes {

    private int id;
    private String workout;

    public WorkoutTypes(int id, String workout) {
        this.id = id;
        this.workout = workout;
    }

    @Override
    public String toString() {
        return workout;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

}
