package com.example.jiehe.atracker;

/**
 * Created by jiehe on 19/03/2018.
 */
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Map;

public class GraphFragment extends Fragment {

  //example chart object
  LineChart myChart;
  FileManager fm;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    //return super.onCreateView(inflater, container,savedInstanceState);
    View view = inflater.inflate(R.layout.frag_graph, container, false);

    //initialised myChart object
    myChart = (LineChart)view.findViewById(R.id.lineChartExample);

    //example of adding data point
    plotRecords();

    return view;
  }

  /**
   * Plots a graph of start time (on x coord) against duration (on y coord)
   */
  private void plotRecords() {
    //load up the records from data base, the parameters are the start and end time of selection
    ArrayList<SingleActivityRecord> sSAR = fm.getRecords(0, Long.MAX_VALUE);

    //by documentation, you need to use Entry objects for the LineChart object to gen plot
    ArrayList<Entry> entries = new ArrayList<>();

    //load up the records into coordinates
    for(SingleActivityRecord s : sSAR){
      entries.add(new Entry(s.getStartTime(), s.getDuration()));
    }

    //convert into lineDataSet
    LineDataSet dataSet = new LineDataSet(entries, "Demo");
    //set colour... they want int as param.. so i stick 2 in
    dataSet.setCircleColor(2);
    dataSet.setValueTextColor(2);

    //convert again...
    LineData lineData = new LineData(dataSet);
    //set to display
    myChart.setData(lineData);
    //refresh display
    myChart.invalidate();
  }

  /**
   * Sets the FileManager
   * @param fm
   *  FileManager object to be used
   */
  public void setFileManager(FileManager fm){
    this.fm = fm;
  }


}