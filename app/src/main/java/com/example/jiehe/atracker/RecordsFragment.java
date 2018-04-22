package com.example.jiehe.atracker;

/**
 * Created by jiehe on 19/03/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class RecordsFragment extends Fragment {

  private Button btnCreate;
  private ListView recordList;
  private FileManager fm;

  public void setFM(FileManager fm){
    this.fm = fm;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    //return super.onCreateView(inflater, container,savedInstanceState);
    View view = inflater.inflate(R.layout.frag_list, container, false);

    btnCreate = (Button)view.findViewById(R.id.btnAddGoal);
    recordList = (ListView)view.findViewById(R.id.listGoal);
    btnCreate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar s = Snackbar.make(view, "◉_◉: Code not found!", Snackbar.LENGTH_LONG);
        s.show();
      }
    });

    loadRecords();

    return view;
  }

  private void loadRecords() {
    ArrayList<SingleActivityRecord> sar = fm.getRecords(0, Long.MAX_VALUE, "");

    if(sar.size() > 0 ){
      RecordAdapter aAdapter = new RecordAdapter(sar, this.getContext(), fm);

      recordList.setAdapter(aAdapter);
    }

  }

}