package com.example.todo_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.*;
import androidx.fragment.app.Fragment;

import com.example.todo_app.*;
import com.example.todo_app.Model.ToDoModel;
import com.example.todo_app.Utils.DatabaseHelper;

public class FirstFragment extends Fragment {

    public FirstFragment(){
        // require a empty public constructor
    }
    DatabaseHelper DB;
    Context thiscontext;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_first, container, false);
        thiscontext=view.getContext();
        // calling the database
        DB = new DatabaseHelper(thiscontext);
        Cursor res = DB.getdata();
        int counter=res.getCount();
        int duecounter=0;
        while (res.moveToNext()) {
            ToDoModel task = new ToDoModel();
            if(res.getInt(3)==0){
                duecounter++;
            }

        }


        TextView val=(TextView)view.findViewById(R.id.todocount);
        val.setText(Integer.toString(counter));
        TextView duebox=(TextView)view.findViewById(R.id.duecount);
        duebox.setText(Integer.toString(duecounter));
        return view;
    }
}
