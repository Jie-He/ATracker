package com.example.jiehe.atracker;

/**
 * Created by Adham on 3/23/2018.
 */


public class Goal {

    private int goal;
    private boolean aob; //true if goal is to be met, false if goal isn't to be exceeded

    public Goal(){
        goal = 0;
        aob = true;
    }

    public Goal getGoal() {
        return this;
    }
    public int getActualGoal() {
        return goal;
    }

    public boolean getAob() {
        return aob;
    }

    public void setGoal(int g, boolean b) {
        goal = g;
        aob = b;
    }

    public void setAob(boolean b){
        aob = b;
    }
    public boolean goalMet(int value) {
        if(aob && value>= goal) {
            return true;
        }
        else if(aob == false && value <= goal) {
            return true;
        }
        else {
            return false;
        }
    }

}

