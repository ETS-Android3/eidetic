package com.example.todo_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.*;
import java.util.Objects;

import androidx.fragment.app.Fragment;

import com.example.todo_app.*;

public class fourthFragment extends Fragment {

    public fourthFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);
        TextView head=(TextView) view.findViewById(R.id.textView3);
//        head.setText("hello tirtha!");
        return view;
    }
}
