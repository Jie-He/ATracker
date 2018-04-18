package com.example.jiehe.atracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    long dateInMillis;
    int year = 0;
    int month = 0;
    int day = 0;
    Button myButton;
    //create
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        if(year == 0) {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            dateInMillis = 0;
        }
        DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
        d.getDatePicker().setMaxDate(System.currentTimeMillis());
        // Create a new instance of DatePickerDialog and return it
        return d;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        this.year = year;
        this.month = month;
        this.day = day;
        c.set(year, month, day);
        dateInMillis =  c.getTimeInMillis();
        Log.d("DUDE", "Time is : "+ dateInMillis);
        myButton.setText(dateToString());
    }


    public String dateToString(){
        return day + "/" + (month+1) + "/" + year;
    }

    public long getMillis(){
        return dateInMillis;
    }

    public void setButton(Button a){
        myButton = a;
    }
}
