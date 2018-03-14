package com.example.jiehe.atracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_timer, container, false);

        mySpinner = (Spinner)view.findViewById(R.id.activitySpinner);
        lblRecord = (TextView)view.findViewById(R.id.lblRecord);
        btnRecord = (ImageButton) view.findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float deg = btnRecord.getRotation() + 90F;
                btnRecord.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                if(lblRecord.getText().equals("START")){
                    lblRecord.setText("STOP");
                }else{
                    lblRecord.setText("START");
                }
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

}
