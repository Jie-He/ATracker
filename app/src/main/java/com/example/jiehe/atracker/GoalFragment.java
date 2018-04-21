package com.example.jiehe.atracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiehe on 14/03/2018.
 */

public class GoalFragment extends Fragment{

    private Button btnRemove;
    private Button btnCreate;
    private ListView activityList;
    private FileManager fm;

    public NewActivity na;

    public void setFM(FileManager f){
        fm = f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_goal, container, false);

        btnCreate = (Button)view.findViewById(R.id.btnAddGoal);
        activityList = (ListView)view.findViewById(R.id.listGoal);
        activityList.setItemsCanFocus(false);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                na = new NewActivity();
                na.show(getFragmentManager(), "New Activity!");
            }
        });

        loadActivities();
        return view;
    }

    public void loadActivities(){
        ArrayList<MyActivity> ma = fm.getActivity();
        if(ma.size() > 0 ){
            ActivityAdapter aAdapter = new ActivityAdapter(ma, this.getContext(), fm);

            activityList.setAdapter(aAdapter);
        }
    }
}
