package com.example.todo_app;

import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
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
        ImageButton reloadbtn = (ImageButton) view.findViewById(R.id.reload);
        reloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshsecondFragment();
            }
        });
        DB = new DatabaseHelper(thiscontext);
        Cursor res = DB.getdata();
        if (res.getCount() == 0) {
            Toast.makeText(thiscontext, "whoohoo! No ToDo Present", Toast.LENGTH_SHORT).show();

        } else {

            while (res.moveToNext()) {
                ToDoModel task = new ToDoModel();
                task.setId(res.getInt(0));
                task.setTask(res.getString(1));
                task.setStatus(0);
                task.setDate(getCurDate());

                taskList.add(task);
                System.out.println(res.getInt(0) + res.getString(1));
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

    private void getChecked(View view) {
        Cursor res = DB.getdata();
        RecyclerView checkRecyclerView = (RecyclerView) view.findViewById(R.id.taskRecyclerview);
        checkRecyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));
        ToDoAdapter checkAdapter = new ToDoAdapter(this);
        checkRecyclerView.setAdapter(checkAdapter);
        for (int i = 0; i < res.getCount(); i++) {
            View card = checkRecyclerView.getChildAt(i);
            CheckBox status = (CheckBox) view.findViewById(R.id.todoCheckbox);
            System.out.println(status.isChecked());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCurDate() {
        Calendar c = Calendar.getInstance();
        String date = makeDoubledigit(c.get(Calendar.DAY_OF_MONTH)) + "/" + makeDoubledigit(c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR) +
                "\n" + makeDoubledigit(c.get(Calendar.HOUR_OF_DAY)) + ":" + makeDoubledigit(c.get(Calendar.MINUTE));
        return date;

    }
    public String  makeDoubledigit(int a){
        if(a<10){
            return "0"+a;
        }
        else{
            return ""+a;
        }
    }


}
