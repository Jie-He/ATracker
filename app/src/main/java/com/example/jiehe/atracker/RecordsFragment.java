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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;

public class RecordsFragment extends Fragment {

  private Button btnCreate;
  private ListView recordList;
  private FileManager fm;
  private Spinner sortOption;

  Boolean order = false;

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

    sortOption = (Spinner)view.findViewById(R.id.sortSpinner);
    ArrayList<String> options = new ArrayList<>();
    options.add("Sort by name");
    options.add("Sory by duration");
    options.add("Sort by date");

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getActivity(), R.layout.spinner_item, options);

    sortOption.setAdapter(adapter);

    sortOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        loadRecords(sortOption.getSelectedItem().toString(), !order);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
        //shouldnt happen.
        loadRecords(sortOption.getSelectedItem().toString(), !order);
      }
    });


    loadRecords("", order);

    return view;
  }


  private void loadRecords(String sortBy, boolean order) {
    ArrayList<SingleActivityRecord> sar = new ArrayList<>();
    sar = fm.getRecords(0, Long.MAX_VALUE, "");

    if(sortBy.length() > 0){
      quickSort(sar, sortBy, order, 0, sar.size()-1);
    }

    if(sar.size() > 0 ){
      RecordAdapter aAdapter = new RecordAdapter(sar, this.getContext(), fm);

      recordList.setAdapter(aAdapter);
    }

  }


  public void quickSort(ArrayList<SingleActivityRecord> sSar, String mode ,boolean order, int fPointer, int rPointer){
    if(fPointer < rPointer){
      int oRPointer = rPointer;
      int oFPointer = fPointer;
      if(order){
        while(fPointer < rPointer){
          if(compare(mode, order, sSar.get(fPointer), sSar.get(rPointer))){
            switchN(sSar, rPointer, rPointer-1);
            if(fPointer != rPointer-1){
              switchN(sSar, fPointer, rPointer);
            }
            rPointer--;
          }else{
            fPointer++;
          }
        }
      }else{
        while(fPointer < rPointer){
          if(sSar.get(fPointer).getStartTime() < sSar.get(rPointer).getStartTime()){
            switchN(sSar, rPointer, rPointer-1);
            if(fPointer != rPointer-1){
              switchN(sSar, fPointer, rPointer);
            }
            rPointer--;
          }else{
            fPointer++;
          }
        }
      }

      //switchNames(names, fPointer, rPointer);
      quickSort(sSar, mode, order, oFPointer, fPointer-1);
      quickSort(sSar,mode, order, rPointer+1, oRPointer);
    }
  }

  public boolean compare(String mode, boolean order, SingleActivityRecord a, SingleActivityRecord b){
    boolean result = false;

    if(mode.equals("Sort by date")){
      if(a.getStartTime() > b.getStartTime()){
        result = true;
      }
    }else if(mode.equals("Sort by name")){
      if(fm.getActivityName(a.getActivity_id()).compareTo(fm.getActivityName(b.getActivity_id())) > 0){
        result = true;
      }
    }else{
      //duration
      if(a.getDuration() > b.getDuration()){
        result = true;
      }
    }

    if(order){
      return !result;
    }
    return result;
  }

  public void switchN(ArrayList<SingleActivityRecord> sSar, int fIndex, int rIndex){
    SingleActivityRecord sar = sSar.get(fIndex);
    sSar.set(fIndex, sSar.get(rIndex));
    sSar.set(rIndex,  sar);
  }

}