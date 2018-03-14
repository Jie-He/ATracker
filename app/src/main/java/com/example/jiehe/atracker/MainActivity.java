package com.example.jiehe.atracker;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageButton btnTimer;
    ImageButton btnGraph;
    ImageButton btnSetting;
    ImageButton btnGoal;

    TextView lblTitleBar;
    TimerFragment mainTimerFrag;
    GraphFragment mainGraphFrag;
    SettingFragment mainSettingFrag;
    GoalFragment mainGoalFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTimerFrag = new TimerFragment();
        mainGraphFrag = new GraphFragment();
        mainSettingFrag = new SettingFragment();
        mainGoalFrag = new GoalFragment();

        lblTitleBar = (TextView)findViewById(R.id.titleBarLabel);

        btnTimer = (ImageButton)findViewById(R.id.btnTimer);
        btnGraph = (ImageButton)findViewById(R.id.btnGraph);
        btnSetting = (ImageButton)findViewById(R.id.btnSetting);
        btnGoal = (ImageButton)findViewById(R.id.btnGoals);

        btnTimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrage(1);
            }
        });

        btnGraph.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrage(2);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrage(3);
            }
        });

        btnGoal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeFrage(4);
            }
        });
    }

    public void changeFrage(int id){
        btnTimer.setBackgroundColor(Color.argb(0,0,0,0));
        btnSetting.setBackgroundColor(Color.argb(0,0,0,0));
        btnGraph.setBackgroundColor(Color.argb(0,0,0,0));
        btnGoal.setBackgroundColor(Color.argb(0,0,0,0));

        Fragment target = null;
        switch (id){
            case 1:
                btnTimer.setBackgroundColor(Color.GRAY);
                target = mainTimerFrag; lblTitleBar.setText("Activity");break;
            case 2:
                btnGraph.setBackgroundColor(Color.GRAY);
                target = mainGraphFrag;lblTitleBar.setText("Dashboard");break;
            case 3:
                btnSetting.setBackgroundColor(Color.GRAY);
                target = mainSettingFrag;lblTitleBar.setText("Settings");break;
            case 4:
                btnGoal.setBackgroundColor(Color.GRAY);
                target = mainGoalFrag;lblTitleBar.setText("Goals");break;

        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.active_frag, target);
        transaction.commit();
    }
}
