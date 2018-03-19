package com.example.jiehe.atracker;

import android.util.Log;

/**
 * Created by Adam on --/03/2018.
 */

//logs start and stop times of an activity and returns these values & the difference between them

public class SingleActivityRecord {

    private int activity_id;
    private long startTime;
    private long endTime;
    private boolean isRunning;

    public SingleActivityRecord(){
        isRunning = false;
        startTime = 0;
        endTime = 0;
    }

    public void startActivity(){
        if(!isRunning){
            startTime = System.currentTimeMillis();
            Log.d("test", Long.toString(startTime));
            isRunning = true;
        }
    }

    public void endActivity(){
        if(isRunning){
            endTime = System.currentTimeMillis();
            isRunning = false;
        }
    }

    public long getStartTime(){
        return startTime;
    }

    public long getEndTime(){
        return endTime;
    }

    public long getDuration(){
        if(isRunning){
            return (long)System.currentTimeMillis() - startTime;
        }

        return endTime - startTime;
    }

    public int getActivity_id(){
        return activity_id;
    }

    public void setActivity_id(int id){
        activity_id = id;
    }

}
