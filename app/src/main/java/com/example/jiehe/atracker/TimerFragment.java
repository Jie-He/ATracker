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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_timer, container, false);

        isRecording = false;
        myConverter = new TimeConverter();
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

        ArrayList<String> activityList = new ArrayList<String>();
        activityList.add("WORKING");
        activityList.add("STUDYING");
        activityList.add("GAMING");
        activityList.add("MORE..");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, activityList);

        mySpinner.setAdapter(adapter);


        return view;
    }

    public void btnRecordClick(){
        float deg = btnRecord.getRotation() + 90F;
        btnRecord.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
        if(isRecording){
            timeThread.interrupt();
            lblRecord.setText("START");
            myRecord.endActivity();
            mySpinner.setEnabled(true);
            //save this activity after; maybe pass to super class and deal with it
        }else{
            lblRecord.setText("STOP");
            myRecord = new SingleActivityRecord();
            myRecord.startActivity();
            createTimeThread();
            mySpinner.setEnabled(false);
            //
        }
        isRecording = !isRecording;
    }

    public void createTimeThread(){
        timeThread = new Thread(){
            public void run(){
                boolean r = true;
                boolean flipflop = true;
                while(r){
                    try{
                        lblDuration.setText(myConverter.getTimeString(myRecord.getDuration(), flipflop));
                        flipflop = !flipflop;
                        Thread.sleep(333); //sleep for 3rd of a second
                    } catch (InterruptedException e){
                        r = false;
                    }
                }
            }
        };
        timeThread.start();
    }
}
