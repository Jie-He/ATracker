package com.example.jiehe.atracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerFragment extends Fragment {
    Spinner mySpinner;
    ImageButton btnRecord;
    TextView lblRecord;
    TextView lblDuration;

    TimeConverter myConverter;
    SingleActivityRecord myRecord;

    Thread timeThread;
    boolean isRecording;

    FileManager fm;

    /**
     * Constructor.
     * Set the isRecording variable and the Timer.
     */
    public TimerFragment(){
      isRecording = false;
      myConverter = new TimeConverter();
    }

    /**
     * returns the view of the fragment and set all the UI elements
     * @param inflater
     *  dunno.
     * @param container
     *  the class contains this?
     * @param savedInstanceState
     *  dunnn
     * @return
     *  the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_timer, container, false);

        //get all the UI components
        mySpinner = (Spinner)view.findViewById(R.id.activitySpinner);
        lblRecord = (TextView)view.findViewById(R.id.lblRecord);
        lblDuration = (TextView)view.findViewById(R.id.lblTime);
        btnRecord = (ImageButton) view.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRecordClick();
            }
        });

        //add default activities.
        //will remove this and load from database when complete
        ArrayList<String> activityList = new ArrayList<String>();
        activityList.add("WORKING");
        activityList.add("STUDYING");
        activityList.add("GAMING");
        activityList.add("MORE..");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, activityList);

        mySpinner.setAdapter(adapter);

        //update the label incase timer already started.
        updateLabelAndSpinner();

        return view;
    }

    /**
     * Sets the FileManager
     * @param fm
     *  FileManager object to be used
     */
    public void setFileManager(FileManager fm){
        this.fm = fm;
    }

    /**
     * Record button press.
     */
    public void btnRecordClick(){
        //rotation animation.. probs remove when button texture made
        float deg = btnRecord.getRotation() + 90F;
        btnRecord.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

        //what to do when its recording
        if(isRecording){ //stop the thread
            timeThread.interrupt();
            myRecord.endActivity(); //stop activity
            //save this activity after; maybe pass to super class and deal with it
            if(fm != null){ //add to database
                fm.updateRecord(myRecord, myRecord);
            }

        }else{
            //make a new record
            myRecord = new SingleActivityRecord();
            myRecord.startActivity(); //start the activity
            if(fm != null){
                fm.addRecord(myRecord);
            }
            createTimeThread(); //start the thread
        }
        //change the recording state
        isRecording = !isRecording;
        //update the labels and lock the spinner
        updateLabelAndSpinner();

    }

    /**
     * Method that updates the label and the spinner.
     */
    public void updateLabelAndSpinner(){
        if(isRecording){
            lblRecord.setText("STOP");
            mySpinner.setEnabled(false);
        }else{
            lblRecord.setText("START");
            mySpinner.setEnabled(true);
        }
    }

    /**
     * Start a thread that updates the UI label for time
     * Sleeps 500ms.
     */
    public void createTimeThread(){
        timeThread = new Thread(){
            public void run(){
                boolean r = true;
                boolean flipflop = true;
                while(r){
                    try{
                        lblDuration.setText(myConverter.getTimeString(myRecord.getDuration(), flipflop));
                        flipflop = !flipflop;
                        Thread.sleep(500); //sleep for half of a second
                    } catch (InterruptedException e){
                        r = false;
                    }
                }
            }
        };
        timeThread.start();
    }
}
