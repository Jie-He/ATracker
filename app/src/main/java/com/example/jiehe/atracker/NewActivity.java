package com.example.jiehe.atracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewActivity extends DialogFragment {

    private EditText input1;
    private EditText input2;
    private CheckBox input3;

    int hours;
    String name;
    boolean aob;

    public interface NewActivityListener{
        public void onDialogPositiveClick(NewActivity dialog);
        public void onDialogNegativeClick(NewActivity dialog);
    }

    NewActivityListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener = (NewActivityListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement NewActivityListner");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get the 2 text boxes

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.new_activity_input, null);

        input1 = (EditText)view.findViewById(R.id.input_1);
        input2 = (EditText)view.findViewById(R.id.input_2);
        input3 = (CheckBox)view.findViewById(R.id.checkInput1);
        input1.setHint("Activity Name");
        input2.setHint("Daily Goal (Hours)");

        builder.setTitle("New Activity")
                .setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //validations
                        Snackbar s;
                        Log.d("New Activity", "We Tried!");
                        if(input1.getText().toString().length() > 0){
                           try{
                                int shours = Integer.parseInt(input2.getText().toString());
                                if(shours > 24){
                                    hours = 24;
                                }else{
                                    hours = shours;
                                }

                                name = input1.getText().toString();
                                aob = input3.isChecked();
                                mListener.onDialogPositiveClick(NewActivity.this);
                            }catch (Exception e){

                                mListener.onDialogNegativeClick(NewActivity.this);

                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(NewActivity.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
