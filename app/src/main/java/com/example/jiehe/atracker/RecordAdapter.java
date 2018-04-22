package com.example.jiehe.atracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordAdapter extends BaseAdapter implements ListAdapter {
  private ArrayList<SingleActivityRecord> list;
  private Context context;
  private FileManager fm;

  public RecordAdapter(ArrayList<SingleActivityRecord> list, Context context, FileManager fm){
    this.list = list;
    this.context = context;
    this.fm = fm;
  }
  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(int i) {
    return list.get(i);
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(final int i, View cview, ViewGroup viewGroup) {
    View view = cview;
    if(view == null){
      LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.custom_record_view, null);
    }

    final TextView actName = (TextView)view.findViewById(R.id.lbl_custom_actName);
    actName.setText(fm.getActivityName(list.get(i).getActivity_id()));
    TimeConverter tc = new TimeConverter();

    final TextView startTime = (TextView)view.findViewById(R.id.lbl_custom_startTime);
    startTime.setText(tc.getDateTimeString(list.get(i).getStartTime()));

    final TextView duration =  (TextView)view.findViewById(R.id.lbl_custom_duration);
    duration.setText(tc.getTimeString(list.get(i).getDuration()));

    final Button btnRemove = (Button)view.findViewById(R.id.btn_record_remove);
    btnRemove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fm.deleteRecord(list.get(i));
        list.remove(i);
        notifyDataSetChanged();
      }
    });



    return view;
  }
}
