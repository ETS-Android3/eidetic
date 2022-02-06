package com.example.todo_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.*;
import androidx.fragment.app.Fragment;

import com.example.todo_app.*;

public class ThirdFragment extends Fragment {

    View view;
    public ThirdFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_third, container, false);
        Switch Switch1=(Switch) view.findViewById(R.id.noti_toggle1);
        Switch Switch2=(Switch) view.findViewById(R.id.noti_toggle2);
        Switch Switch3=(Switch) view.findViewById(R.id.noti_toggle3);




        Switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(view.getContext(), "You will get notification if you create a Task.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "You will not get notification if you create a Task.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(view.getContext(), "You will get notification if you complete a Task.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "You will not get notification if you complete a Task.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(view.getContext(), "You will get notification if you remove or update a Task.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "You will not get notification if you remove or update a Task.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}
