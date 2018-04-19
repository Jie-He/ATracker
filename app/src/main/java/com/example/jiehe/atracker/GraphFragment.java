package com.example.jiehe.atracker;

/**
 * Created by jiehe on 19/03/2018.
 */
import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.Map;

public class GraphFragment extends Fragment {

  //example chart object
  BarChart myChart;
  FileManager fm;

  Button btnPickDate1;
  Button btnPickDate2;
  Button btnGen;
  DatePickerFragment Date1;
  DatePickerFragment Date2;

  public GraphFragment(){
    Date1 = new DatePickerFragment();
    Date2 = new DatePickerFragment();
  }


  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    //return super.onCreateView(inflater, container,savedInstanceState);
    View view = inflater.inflate(R.layout.frag_graph, container, false);

    //initialised myChart object
    myChart = (BarChart) view.findViewById(R.id.BarChartExample);

    btnPickDate1 = (Button)view.findViewById(R.id.btnPickDateOne);
    btnPickDate1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Date1.setButton(btnPickDate1);
        Date1.show( getActivity().getSupportFragmentManager(), "Start Date");
      }
    });

    btnPickDate2 = (Button)view.findViewById(R.id.btnPickDateTwo);
    btnPickDate2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Date2.setButton(btnPickDate2);
        Date2.show( getActivity().getSupportFragmentManager(), "End Date");
      }
    });

    btnGen = (Button)view.findViewById(R.id.btnGenGraph);
    btnGen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        genGraph();
      }
    });

    if(Date1.dateInMillis != 0){
      btnPickDate1.setText("From: " + Date1.dateToString());
    }

    if(Date2.dateInMillis != 0 ){
      btnPickDate2.setText("To: " + Date2.dateToString());
    }


    return view;
  }

  /**
   * Plots a graph of start time (on x coord) against duration (on y coord)
   */
  private void plotRecords(long lBound, long uBound) {
    //load up the records from data base, the parameters are the start and end time of selection
    ArrayList<SingleActivityRecord> sSAR = new ArrayList<>();
    ArrayList<MyActivity> aActivity = fm.getActivity();
    //by documentation, you need to use Entry objects for the LineChart object to gen plot
    ArrayList<BarEntry> entries = new ArrayList<>();
    long sum = 0;
    for(int i = 0; i < aActivity.size(); i ++){
      sum = 0;
      sSAR = fm.getRecords(lBound, uBound, aActivity.get(i).getName());
      for(int j = 0; j < sSAR.size(); j++){
         sum += sSAR.get(j).getDuration();
      }
      entries.add(new BarEntry(i, sum));
    }

    if(entries.size() > 0){

      //convert into lineDataSet
      BarDataSet dataSet = new BarDataSet(entries, "Demo");
      BarData data = new BarData(dataSet);
      data.setBarWidth(0.9f);

      //set to display
      myChart.setData(data);
      myChart.setFitBars(true);
      //refresh display
      myChart.invalidate();
    }else{
      Snackbar s = Snackbar.make(this.getView(), "No records found!", Snackbar.LENGTH_LONG);
      s.show();
    }
  }

  /**
   * Sets the FileManager
   * @param fm
   *  FileManager object to be used
   */
  public void setFileManager(FileManager fm){
    this.fm = fm;
  }

  public void genGraph(){
    Snackbar mySnackbar = Snackbar.make(this.getView(), "Please fix the dates...", Snackbar.LENGTH_LONG);
    if(Date1.dateInMillis > 0){
      if(Date2.dateInMillis == 0){
        plotRecords(Date1.dateInMillis, Long.MAX_VALUE);
      }else if(Date1.dateInMillis < Date2.dateInMillis){
        plotRecords(Date1.dateInMillis, Date2.dateInMillis);
      }else{
        mySnackbar.show();
      }
    }else{
      mySnackbar.show();

    }
  }


}