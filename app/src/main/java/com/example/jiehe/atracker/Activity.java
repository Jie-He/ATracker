package com.example.jiehe.atracker;

import java.util.ArrayList;

/**
 * Created by jiehe on 18/03/2018.
 */

public class Activity {
    private String myName;
    private ArrayList<SingleActivityRecord> myRecords;
    private int myGoal;
    private int myGoalMode;

    public Activity(String name){
        this.myName = name;
        myRecords = new ArrayList<>();
    }

    public void addRecord(SingleActivityRecord sar){
        myRecords.add(sar);
    }

    public String getName() {
        return myName;
    }

    public void setName(String myName) {
        this.myName = myName;
    }

    public int getGoal() {
        return myGoal;
    }

    public void setGoal(int myGoal) {
        this.myGoal = myGoal;
    }

    public int getGoalMode() {
        return myGoalMode;
    }

    public void setGoalMode(int myGoalMode) {
        this.myGoalMode = myGoalMode;
    }
}
