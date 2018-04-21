package com.example.jiehe.atracker;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NewActivity.NewActivityListener{

    private boolean DEV_MODE = true;

    ImageButton btnTimer;
    ImageButton btnGraph;
    ImageButton btnSetting;
    ImageButton btnGoal;
    ImageButton btnRecords;
    ImageButton btnDebug;

    TextView lblTitleBar;

    TimerFragment mainTimerFrag;
    GraphFragment mainGraphFrag;
    SettingFragment mainSettingFrag;
    GoalFragment mainGoalFrag;
    RecordsFragment mainRecordsFrag;
    DebugFragment mainDebugFrag;

    FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = new FileManager(this, null);

        //add the default activities
        fm.addActiviy(new MyActivity("FREE TIME"));
        fm.addActiviy(new MyActivity("TRAVEL"));
        fm.addActiviy(new MyActivity("WORK"));
        fm.addActiviy(new MyActivity("EXERCISE"));
        fm.addActiviy(new MyActivity("SLEEP"));

        mainTimerFrag = new TimerFragment(); mainTimerFrag.setFileManager(fm);
        mainGraphFrag = new GraphFragment(); mainGraphFrag.setFileManager(fm);
        mainSettingFrag = new SettingFragment();
        mainGoalFrag = new GoalFragment(); mainGoalFrag.setFM(fm);
        mainRecordsFrag = new RecordsFragment();
        mainDebugFrag = new DebugFragment(); mainDebugFrag.setFileManager(fm);

        lblTitleBar = (TextView)findViewById(R.id.titleBarLabel);

        //get all buttons
        btnTimer = (ImageButton)findViewById(R.id.btnTimer);
        btnGraph = (ImageButton)findViewById(R.id.btnGraph);
        btnSetting = (ImageButton)findViewById(R.id.btnSetting);
        btnGoal = (ImageButton)findViewById(R.id.btnGoals);
        btnRecords = (ImageButton)findViewById(R.id.btnRecords);
        btnDebug = (ImageButton)findViewById(R.id.btnDebug);

        //add listeners to button...
        btnTimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrag(1);
            }
        });
        btnGraph.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrag(2);
            }
        });
        btnGoal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrag(3);
            }
        });
        btnRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFrag(4);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrag(5);
            }
        });
        btnDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFrag(9);
            }
        });

        if(!DEV_MODE){
            btnDebug.setVisibility(View.GONE);
        }
    }

    public void changeFrag(int id){
        //set all button back colour to nothing.
        btnTimer.setBackground(null);
        btnSetting.setBackground(null);
        btnGraph.setBackground(null);
        btnGoal.setBackground(null);
        btnRecords.setBackground(null);
        btnDebug.setBackground(null);

        Fragment target = null;

        switch (id){
            case 1://show the timer
                btnTimer.setBackground(ContextCompat.getDrawable(this, R.drawable.nav_button_back));
                target = mainTimerFrag; lblTitleBar.setText("Activity");break;
            case 2: //show the graph
                btnGraph.setBackground(ContextCompat.getDrawable(this, R.drawable.nav_button_back));
                target = mainGraphFrag;lblTitleBar.setText("Dashboard");break;
            case 3://show the goals
                btnGoal.setBackground(ContextCompat.getDrawable(this, R.drawable.nav_button_back));
                target = mainGoalFrag;lblTitleBar.setText("Goals");break;
            case 4: //show the records
                btnRecords.setBackground(ContextCompat.getDrawable(this, R.drawable.nav_button_back));
                target = mainRecordsFrag;lblTitleBar.setText("Records");break;
            case 5: //show the setting view
                btnSetting.setBackground(ContextCompat.getDrawable(this, R.drawable.nav_button_back));
                target = mainSettingFrag;lblTitleBar.setText("Settings");break;
            case 9: //show the debug view
                btnDebug.setBackground(ContextCompat.getDrawable(this, R.drawable.nav_button_back));
                target = mainDebugFrag;lblTitleBar.setText("Debug Page");break;

        }
        //change the fragment.
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.active_frag, target);
        transaction.commit();
    }

    @Override
    public void onDialogPositiveClick(NewActivity dialog) {
        if(dialog != null){
            Goal g = new Goal();
            g.setGoal(dialog.hours, dialog.aob);
            MyActivity ma = new MyActivity(dialog.name.toUpperCase(), g);
            fm.addActiviy(ma);

            try{
                mainTimerFrag.loadNameIntoSpinner();
            }catch (Exception e){
            }

            try{
                mainGoalFrag.loadActivities();
            }catch(Exception e){

            }
        }
    }

    @Override
    public void onDialogNegativeClick(NewActivity dialog) {
        Snackbar s = Snackbar.make(findViewById(android.R.id.content), "◉_◉ : Something wrong...", Snackbar.LENGTH_SHORT);
        s.show();
    }
}
