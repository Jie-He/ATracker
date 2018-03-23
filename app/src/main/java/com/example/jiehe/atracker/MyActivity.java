package com.example.jiehe.atracker;

import java.util.ArrayList;

/**
 * Created by jiehe on 18/03/2018.
 */

public class MyActivity {
    private String myName;
    private ArrayList<SingleActivityRecord> myRecords;
    private Goal myGoal;

    public MyActivity(String name){
        this.myName = name.toUpperCase();
        myGoal = new Goal();
        myRecords = new ArrayList<>();
    }

    public MyActivity(String name, Goal goal){
        this.myName = name;
        this.myGoal = goal;
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

    public int getActualGoal() {
        return myGoal.getActualGoal();
    }

    public Goal getGoal(){return myGoal;}

    public void setGoal(Goal myGoal) {
        this.myGoal = myGoal;
    }

    public boolean getGoalMode() {
        return myGoal.getAob();
    }

    public void setGoalMode(boolean b) {
        myGoal.setAob(b);
    }
}
