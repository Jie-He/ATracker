package com.example.jiehe.atracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityAdapter extends BaseAdapter implements ListAdapter{
  private ArrayList<MyActivity> list;
  private Context context;
  private FileManager fm;
  public ActivityAdapter(ArrayList<MyActivity> list, Context context, FileManager fm){
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
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.custom_activity_view, null);
    }

    final EditText actName = (EditText) view.findViewById(R.id.custom_activity_name);
    actName.setText(list.get(i).getName());
    final EditText goalEdit = (EditText)view.findViewById(R.id.custom_goal_edit);
    try{
      goalEdit.setText(list.get(i).getGoal().getActualGoal() + "");
    }catch (Exception e){
      goalEdit.setText("0");
    }
    final CheckBox limitCheck = (CheckBox)view.findViewById(R.id.custom_limit);
    limitCheck.setChecked(list.get(i).getGoal().getAob());

    final TextView lblTimeSpent = (TextView)view.findViewById(R.id.custom_activity_spent);

    //calc the time spent today
    ArrayList<SingleActivityRecord> sar = new ArrayList<>();
    long sum = 0;
    final Calendar c = Calendar.getInstance();
    c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0,0);
    sar = fm.getRecords(c.getTimeInMillis(), Long.MAX_VALUE, list.get(i).getName());

    for(SingleActivityRecord s : sar){
      sum += s.getDuration();
    }

    //convert ms into hours
    double sSum = sum/((double)(60*60*1000));

    lblTimeSpent.setText(String.format( "%.2f", sSum ) + "hrs/" + list.get(i).getGoal().getActualGoal() + "hrs");

    Button btnUpdate = (Button)view.findViewById(R.id.btn_custom_update);
    btnUpdate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Goal newGoal = new Goal();
        try{
          int hour = Integer.parseInt(goalEdit.getText().toString());
          if(hour > 24){
            hour = 24;
          }

          newGoal.setGoal(hour, limitCheck.isChecked());
          MyActivity newActivity = new MyActivity(actName.getText().toString());
          newActivity.setGoal(newGoal);

          fm.updateActivity(list.get(i), newActivity);
          list.remove(i);
          list.add(newActivity);
          notifyDataSetChanged();
        }catch (Exception e){
          Log.d("ACTIVITY CUSTOM", "NOPE");
       }

      }
    });

    Button btnRemove = (Button)view.findViewById(R.id.btn_custom_remove);
    btnRemove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fm.deleteActivity(list.get(i).getName());
        list.remove(i);
        notifyDataSetChanged();
      }
    });

    return view;
  }
}
