package com.example.jiehe.atracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jiehe on 12/03/2018.
 */

public class DebugFragment extends Fragment {

    private FileManager fm;

    //just for test
    private TextView txtRecord;
    private EditText txtInput;
    private Button btnClear;
    private Button btnLoad;
    private Button btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_debug, container, false);

        //load up the text view;
        txtRecord = (TextView)view.findViewById(R.id.txtRecords);
        txtInput = (EditText) view.findViewById(R.id.txtDebugInput);
        btnLoad = (Button)view.findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fm != null){
                    String result = fm.recordToString() + "\n" + fm.activityToString();
                    txtRecord.setText(result);
                }
            }
        });

        btnClear = (Button)view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fm != null){
                    fm.clearDatabase();
                }
            }
        });

        btnAdd = (Button)view.findViewById(R.id.btnDebugAddActivity);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fm != null){
                    String name = txtInput.getText().toString();
                    MyActivity myActivity = new MyActivity(name);
                    fm.addActiviy(myActivity);
                }
            }
        });
        return view;
    }

    public void setFileManager(FileManager fm){
        this.fm = fm;
    }

}