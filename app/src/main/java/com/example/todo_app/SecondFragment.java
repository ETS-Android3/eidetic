package com.example.todo_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.Adapter.ToDoAdapter;
import com.example.todo_app.Model.ToDoModel;
import com.example.todo_app.Utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SecondFragment extends Fragment {

    public SecondFragment() {
        // require a empty public constructor
    }

    Context thiscontext;
    private ToDoAdapter taskAdapter;
    private List<ToDoModel> taskList;
    DatabaseHelper DB;
    View view;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);
        thiscontext = container.getContext();
        taskList = new ArrayList<>();
        RecyclerView taskRecyclerView = (RecyclerView) view.findViewById(R.id.taskRecyclerview);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));
        taskAdapter = new ToDoAdapter(this);
        taskRecyclerView.setAdapter(taskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        // setting reload
        ImageButton reloadbtn = (ImageButton) view.findViewById(R.id.reload);
        reloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshsecondFragment();
            }
        });


        //  calling databasehelper
        DB = new DatabaseHelper(thiscontext);
        Cursor res = DB.getdata();
        if (res.getCount() == 0) {
//            Toast.makeText(thiscontext, "whoohoo! No ToDo Present", Toast.LENGTH_SHORT).show();

        } else {
            ConstraintLayout emptyimage = (ConstraintLayout) view.findViewById(R.id.emptypage);
            emptyimage.setVisibility(View.INVISIBLE);

            while (res.moveToNext()) {
                ToDoModel task = new ToDoModel();
                task.setId(res.getInt(0));
                task.setTask(res.getString(1));
                task.setStatus(res.getInt(3));
                task.setDate(res.getString(2));
                taskList.add(task);
//                System.out.println(res.getInt(0) + res.getString(1) + res.getString(2));
            }
        }


        taskAdapter.setTasks(taskList);


        return view;
    }

    private void refreshsecondFragment() {
        SecondFragment fragment = new SecondFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }


}
