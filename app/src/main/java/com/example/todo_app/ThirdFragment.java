package com.example.todo_app;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.*;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.todo_app.*;
import com.example.todo_app.Utils.DatabaseHelper;

public class ThirdFragment extends Fragment {

    View view;
    DatabaseHelper DB;
    int switch1_state;
    int switch2_state;
    int switch3_state;
    public ThirdFragment(){
        // require a empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_third, container, false);
        Switch Switch1=(Switch) view.findViewById(R.id.noti_toggle1);
        Switch Switch2=(Switch) view.findViewById(R.id.noti_toggle2);
        Switch Switch3=(Switch) view.findViewById(R.id.noti_toggle3);

        DB = new DatabaseHelper(view.getContext());
        Cursor res = DB.getNotidata();
        if (res.getCount() == 0) {
            Toast.makeText(view.getContext(), "No noti found", Toast.LENGTH_SHORT).show();
            DB.insertNotidetails();
        }
        else{
            while (res.moveToNext()) {
                switch1_state=res.getInt(1);
                switch2_state=res.getInt(2);
                switch3_state=res.getInt(3);
                Switch1.setChecked(getIntToBool(switch1_state));
                Switch2.setChecked(getIntToBool(switch2_state));
                Switch3.setChecked(getIntToBool(switch3_state));

                System.out.println(res.getInt(0)+""+switch1_state+""+switch2_state+""+switch3_state);

//                System.out.println(res.getInt(0) + res.getString(1) + res.getString(2));
            }
        }


        Switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(view.getContext(), "You will get notification if you create a Task.", Toast.LENGTH_SHORT).show();
                    DB.updateNotistatus(1,"switch1");
                }
                else{
                    Toast.makeText(view.getContext(), "You will not get notification if you create a Task.", Toast.LENGTH_SHORT).show();
                    DB.updateNotistatus(0,"switch1");
                }
            }
        });

        Switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(view.getContext(), "You will get notification if you complete a Task.", Toast.LENGTH_SHORT).show();
                    DB.updateNotistatus(1,"switch2");
                }
                else{
                    Toast.makeText(view.getContext(), "You will not get notification if you complete a Task.", Toast.LENGTH_SHORT).show();
                    DB.updateNotistatus(0,"switch2");
                }
            }
        });

        Switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(view.getContext(), "You will get notification if you remove or update a Task.", Toast.LENGTH_SHORT).show();
                    System.out.println(DB.updateNotistatus(1,"switch3"));
                }
                else{
                    Toast.makeText(view.getContext(), "You will not get notification if you remove or update a Task.", Toast.LENGTH_SHORT).show();
                    System.out.println(DB.updateNotistatus(0,"switch3"));
                }
            }
        });


        return view;
    }


    private Boolean getIntToBool(int i){
        return i!=0;
    }
}
