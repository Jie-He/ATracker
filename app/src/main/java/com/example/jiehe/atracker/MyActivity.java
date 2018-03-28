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

    public int getGoalMode() {
        return myGoal.getAob()? 1 : 0;
    }

    public void setGoalMode(int b) {
        if(b == 1){
            myGoal.setAob(true);
        }else{
            myGoal.setAob(false);
        }

    }
}
