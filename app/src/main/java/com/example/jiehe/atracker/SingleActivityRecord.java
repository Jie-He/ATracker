package com.example.jiehe.atracker;

/**
 * Created by Adam on --/03/2018.
 */

//logs start and stop times of an activity and returns these values & the difference between them

public class SingleActivityRecord {

    private int startTime;
    private int endTime;
    private boolean isRunning;

    public SingleActivityRecord(){
        isRunning = false;
        startTime = 0;
        endTime = 0;
    }

    public void startActivity(){
        if(!isRunning){
            startTime = (int)System.currentTimeMillis();
            isRunning = true;
        }
    }

    public void endActivity(){
        if(isRunning){
            endTime = (int)System.currentTimeMillis();
            isRunning = false;
        }
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public int getDuration(){
        if(isRunning){
            return (int)System.currentTimeMillis() - startTime;
        }

        return endTime - startTime;
    }

}
