package com.example.jiehe.atracker;

/**
 * Created by jiehe on 19/03/2018.
 */
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class GraphFragment extends Fragment {

  //example chart object
  CombinedChart mChart;
  PieChart pChart;
  FileManager fm;

  Button btnPickDate1;
  Button btnPickDate2;
  Button btnGen;
  Button btnGenPie;
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
    mChart = (CombinedChart) view.findViewById(R.id.BarChartExample);


    //Initialise the pie chart
    pChart = (PieChart)view.findViewById(R.id.PieChatExample);

    InitialiseChartObjects();
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

        mChart.setVisibility(View.VISIBLE);
        pChart.setVisibility(View.INVISIBLE);
        genGraphs(1);
      }
    });

    if(Date1.dateInMillis != 0){
      btnPickDate1.setText("From: " + Date1.dateToString());
    }

    if(Date2.dateInMillis != 0 ){
      btnPickDate2.setText("To: " + Date2.dateToString());
    }

    btnGenPie = (Button) view.findViewById(R.id.btnGenPie);
    btnGenPie.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mChart.setVisibility(View.INVISIBLE);
        pChart.setVisibility(View.VISIBLE);
        genGraphs(2);
      }
    });


    return view;
  }

  private void InitialiseChartObjects(){
    mChart.getDescription().setEnabled(false);
    mChart.setBackgroundColor(Color.WHITE);
    mChart.setDrawGridBackground(false);
    mChart.setDrawBarShadow(false);
    mChart.setHighlightFullBarEnabled(false);

    // draw bars behind lines
    mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
            CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
    });

    Legend l = mChart.getLegend();
    l.setWordWrapEnabled(true);
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    l.setDrawInside(false);

    YAxis rightAxis = mChart.getAxisRight();
    rightAxis.setDrawGridLines(false);
    rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

    YAxis leftAxis = mChart.getAxisLeft();
    leftAxis.setDrawGridLines(false);
    leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

    XAxis xAxis = mChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
    xAxis.setAxisMinimum(0f);
    xAxis.setGranularity(1f);

    pChart.setUsePercentValues(true);
    pChart.getDescription().setEnabled(false);
    pChart.setExtraOffsets(5,10,5,5);
    pChart.setDragDecelerationFrictionCoef(0.95f);

    pChart.setDrawHoleEnabled(true);
    pChart.setHoleColor(Color.WHITE);

    pChart.setTransparentCircleColor(Color.WHITE);
    pChart.setTransparentCircleAlpha(110);

    pChart.setHoleRadius(58f);
    pChart.setTransparentCircleRadius(61f);

    pChart.setRotationEnabled(true);
    pChart.setHighlightPerTapEnabled(true);

    pChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    l = pChart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    l.setOrientation(Legend.LegendOrientation.VERTICAL);
    l.setDrawInside(false);
    l.setXEntrySpace(7f);
    l.setYEntrySpace(0f);
    l.setYOffset(0f);

    // entry label styling
    pChart.setEntryLabelColor(Color.WHITE);
    pChart.setEntryLabelTextSize(12f);

  }

  /**
   * Plots a graph of start time (on x coord) against duration (on y coord)
   */
  private BarData getBarData(long lBound, long uBound) {
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
         sum += (sSAR.get(j).getDuration() / 60000);
      }
      entries.add(new BarEntry(i, sum, aActivity.get(i).getName()));
    }

    if(entries.size() > 0){

      //convert into lineDataSet
      BarDataSet dataSet = new BarDataSet(entries, "Total Time Spent");
      BarData data = new BarData(dataSet);
      data.setBarWidth(0.9f);

      //set to display
      return data;
    }else{
      Snackbar s = Snackbar.make(this.getView(), "No records found!", Snackbar.LENGTH_LONG);
      s.show();
      return null;
    }
  }

  private LineData getLineData(){
    ArrayList<MyActivity> aActivity = fm.getActivity();
    LineData d = new LineData();
    ArrayList<Entry> entries = new ArrayList<>();

    if(aActivity.size() > 0){
      for(int i = 0; i < aActivity.size(); i++){
        entries.add( new Entry(i, aActivity.get(i).getActualGoal() * 60));
        Log.d("GRAPH", (aActivity.get(i).getActualGoal() * 60) + " ");
      }
    }else{
      return null;
    }
    Log.d("GRAPH", "HERE");
    LineDataSet set = new LineDataSet(entries, "Goals");
    set.setColor(Color.rgb(240, 238, 70));
    set.setLineWidth(2.5f);
    set.setCircleColor(Color.rgb(240, 238, 70));
    set.setCircleRadius(5f);
    set.setFillColor(Color.rgb(240, 238, 70));
    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    set.setDrawValues(true);
    set.setValueTextSize(10f);
    set.setValueTextColor(Color.rgb(240, 238, 70));

    set.setAxisDependency(YAxis.AxisDependency.LEFT);
    d.addDataSet(set);

    Log.d("GRAPH", "HERE");
    return d;
  }
  /**
   * Sets the FileManager
   * @param fm
   *  FileManager object to be used
   */
  public void setFileManager(FileManager fm){
    this.fm = fm;
  }

  private void genGraphs(int type){
    Snackbar mySnackbar = Snackbar.make(this.getView(), "Please fix the dates...", Snackbar.LENGTH_LONG);
    CombinedData data = new CombinedData();
    long startDate = 0;
    long endDate = 0;
    if(Date1.dateInMillis > 0){
      if(Date2.dateInMillis == 0){
        startDate = Date1.dateInMillis;
        endDate = Long.MAX_VALUE;
      }else if(Date1.dateInMillis < Date2.dateInMillis){
        startDate = Date1.dateInMillis;
        endDate = Date2.dateInMillis;
      }else{
        mySnackbar.show();
        return;
      }
    }else{
      mySnackbar.show();
      return;
    }

    if(type == 1){
      data.setData(getBarData(startDate, endDate));
      data.setData(getLineData());
      mChart.setData(data);
      mChart.invalidate();
    }else{
      pChart.setData(getPieData(startDate, endDate));
      pChart.highlightValue(null);
      mChart.invalidate();
    }
  }

  private PieData getPieData(long lBound, long uBound){
    //load up the records from data base, the parameters are the start and end time of selection
    ArrayList<SingleActivityRecord> sSAR = new ArrayList<>();
    ArrayList<MyActivity> aActivity = fm.getActivity();
    //by documentation, you need to use Entry objects for the LineChart object to gen plot
    ArrayList<PieEntry> entries = new ArrayList<>();
    long sum = 0;
    for(int i = 0; i < aActivity.size(); i ++){
      sum = 0;
      sSAR = fm.getRecords(lBound, uBound, aActivity.get(i).getName());
      for(int j = 0; j < sSAR.size(); j++){
        sum += (sSAR.get(j).getDuration() / 60000);
      }
      entries.add(new PieEntry(sum,aActivity.get(i).getName()));
    }

    PieDataSet dataSet = new PieDataSet(entries, "Activities");
    dataSet.setDrawIcons(false);
    dataSet.setSliceSpace(3f);
    dataSet.setIconsOffset(new MPPointF(0, 40));
    dataSet.setSelectionShift(5f);

    ArrayList<Integer> colors = new ArrayList<Integer>();

    for (int c : ColorTemplate.VORDIPLOM_COLORS)
      colors.add(c);

    for (int c : ColorTemplate.JOYFUL_COLORS)
      colors.add(c);

    for (int c : ColorTemplate.COLORFUL_COLORS)
      colors.add(c);

    for (int c : ColorTemplate.LIBERTY_COLORS)
      colors.add(c);

    for (int c : ColorTemplate.PASTEL_COLORS)
      colors.add(c);

    colors.add(ColorTemplate.getHoloBlue());

    dataSet.setColors(colors);
    //dataSet.setSelectionShift(0f);

    PieData data = new PieData(dataSet);
    data.setValueFormatter(new PercentFormatter());
    data.setValueTextSize(11f);
    data.setValueTextColor(Color.WHITE);

    return data;
  }



}