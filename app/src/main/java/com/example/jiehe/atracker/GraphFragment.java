package com.example.jiehe.atracker;

/**
 * Created by jiehe on 19/03/2018.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GraphFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    //return super.onCreateView(inflater, container,savedInstanceState);
    View view = inflater.inflate(R.layout.frag_graph, container, false);

    return view;
  }

}