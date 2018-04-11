package com.example.jiehe.atracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by jiehe on 14/03/2018.
 */

public class GoalFragment extends Fragment{
    private EditText namebox;
    private EditText goalbox;
    private CheckBox limitcheck;
    private ListView listview2;
    private FileManager fm;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;

    public void setFM(FileManager f){
        fm = f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_goal, container, false);
        namebox = (EditText)view.findViewById(R.id.namebox);
        goalbox = (EditText)view.findViewById(R.id.goalbox);
        limitcheck = (CheckBox)view.findViewById(R.id.limitcheck);
        listview2 = (ListView)view.findViewById(R.id.listview1);
        fab = (FloatingActionButton)view.findViewById(R.id.addactivity);
        fab2 = (FloatingActionButton)view.findViewById(R.id.addactivity2);
        return view;
    }
}
