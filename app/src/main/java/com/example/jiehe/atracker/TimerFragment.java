package com.example.jiehe.atracker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerFragment extends Fragment {
    private Spinner mySpinner;
    private ImageButton btnRecord;
    private TextView lblRecord;
    private TextView lblDuration;

    private TimeConverter myConverter;
    private SingleActivityRecord myRecord;

    private Thread timeThread;
    private boolean isRecording;

    private FileManager fm;
    private final Handler mHandler;
    /**
     * Constructor.
     * Set the isRecording variable and the Timer.
     */
    public TimerFragment(){
      isRecording = false;
      myRecord = new SingleActivityRecord();
      myConverter = new TimeConverter();
      mHandler = new Handler(){
        public void handleMessage(Message msg){
          lblDuration.setText((String)msg.obj);
        }
      };
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
        lblRecord = (TextView)view.findViewById(R.id.lblRecord);
        lblDuration = (TextView)view.findViewById(R.id.lblTime);
        btnRecord = (ImageButton) view.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRecordClick();
            }
        });
        mySpinner = (Spinner)view.findViewById(R.id.activitySpinner);
        //add selection listener
        //add default activities.
        //will remove this and load from database when complete
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(mySpinner.getSelectedItem().toString().equals("Create New...")){
                    //do the new activity business
                    Log.d("Spinner Selection", "Create New Selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //shouldnt happen.
            }
        });

        ArrayList<String> activityList = fm.getActivityNames();
        activityList.add("Create New...");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item, activityList);

        mySpinner.setAdapter(adapter);

        checkUncompleteRecord();
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
            stopRecording();
        }else{
           startRecording(-1,Long.MIN_VALUE);
        }
        //change the recording state
        isRecording = !isRecording;
        //update the labels and lock the spinner
        updateLabelAndSpinner();
    }

    public void checkUncompleteRecord(){
      if(!isRecording) {
        String[] stuff = fm.getDBRecord("SELECT * FROM RECORD_TABLE WHERE END_TIME = '0'");
        if (stuff != null) {
          try{
            String[] activity = fm.getDBRecord("SELECT * FROM ACTIVITY_TABLE WHERE ACTIVITY_ID ='" + stuff[1] + "'");
            mySpinner.setSelection(((ArrayAdapter)mySpinner.getAdapter()).getPosition(activity[1]));
          }catch (Exception e){

          }
          startRecording(Integer.parseInt(stuff[1]), Long.parseLong(stuff[2]));
          //change the recording state
          isRecording = !isRecording;
          //update the labels and lock the spinner
        }
      }
      updateLabelAndSpinner();
    }

    public void startRecording(int act_id, Long start_Time){
        //make a new record
        myRecord = new SingleActivityRecord();
        if(act_id < 0){
            myRecord.setActivity_id(fm.getActivityID(mySpinner.getSelectedItem().toString()));
        }else{
            myRecord.setActivity_id(act_id);
        }

        Log.d("ACT ID", "btnRecordClick: " + myRecord.getActivity_id());
        myRecord.startActivity(); //start the activity

        if(start_Time >=0 ){
            myRecord.setStartTime(start_Time);
        }else{
            //all that necessary stuff/
            if(fm != null){
                fm.addRecord(myRecord);
            }
        }
        createTimeThread(); //start the thread
    }
    public void stopRecording(){
        timeThread.interrupt();
        myRecord.endActivity(); //stop activity
        //save this activity after; maybe pass to super class and deal with it
        if(fm != null){ //add to database
            fm.updateRecord(myRecord, myRecord);
        }
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
                boolean flipflop = true;
                while(true){
                    try{
                      String time = myConverter.getTimeString(myRecord.getDuration(), flipflop);
                      Message msg = Message.obtain();
                      msg.obj = time;
                      msg.setTarget(mHandler);
                      msg.sendToTarget();
                      flipflop = !flipflop;
                      Thread.sleep(500); //sleep for half of a second
                    } catch (InterruptedException e){
                      return;
                    }
                }
            }
        };
        timeThread.start();
    }
}
