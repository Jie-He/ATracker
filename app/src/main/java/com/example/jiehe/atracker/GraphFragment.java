package com.example.jiehe.atracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jiehe on 12/03/2018.
 */

public class GraphFragment extends Fragment {

    private FileManager fm;

    //just for test
    private TextView txtRecord;
    private Button btnLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        //return super.onCreateView(inflater, container,savedInstanceState);
        View view = inflater.inflate(R.layout.frag_graph, container, false);

        //load up the text view;
        txtRecord = (TextView)view.findViewById(R.id.txtRecords);
        btnLoad = (Button)view.findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fm != null){
                    txtRecord.setText(fm.recordToString());
                }
            }
        });
        return view;
    }

    public void setFileManager(FileManager fm){
        this.fm = fm;
    }

}