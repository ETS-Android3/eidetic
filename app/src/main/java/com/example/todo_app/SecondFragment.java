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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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

        // setting delete multiple delete condition
        ImageButton deletebtn = (ImageButton) view.findViewById(R.id.deletechecked);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> checkedlist = getChecked();
                if (checkedlist.size() > 0) {
                    System.out.println(checkedlist.size());
                    createdialog(checkedlist);

                } else {
                    Toast.makeText(thiscontext, "Nothing Selected", Toast.LENGTH_SHORT).show();
                    refreshsecondFragment();
                }
            }
        });

        //  calling databasehelper
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
                task.setDate(res.getString(2));

                taskList.add(task);
//                System.out.println(res.getInt(0) + res.getString(1) + res.getString(2));
            }
        }


        taskAdapter.setTasks(taskList);


        return view;
    }

    private void refreshsecondFragment() {
        RecyclerView mRecView = (RecyclerView) view.findViewById(R.id.taskRecyclerview);
        int size = mRecView.getAdapter().getItemCount();
        System.out.println(size);
        getChecked();
        SecondFragment fragment = new SecondFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }

    private List<Integer> getChecked() {
        RecyclerView mRecView = (RecyclerView) view.findViewById(R.id.taskRecyclerview);
        int size = mRecView.getAdapter().getItemCount();
        System.out.println(size);
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {

//            ToDoAdapter.ViewHolder wordView = (ToDoAdapter.ViewHolder) mRecView.getChildAt(i);
            View wordview=mRecView.getChildAt(i);
//            CheckBox status = (CheckBox) wordView.itemView.findViewById(R.id.todoCheckbox);
            if(wordview==null){
                System.out.println("null at pos "+i);
            }
            else{
                CheckBox status=wordview.findViewById(R.id.todoCheckbox);
                if (status.isChecked()) {
                    list.add(i);
                    System.out.println("yes "+i);
                }
                else{
                    System.out.println("no "+i);
                }

            }




        }
        return list;


    }


    public void deleteItem(int position) {
        ToDoModel item = taskList.get(position);
        int editid = item.getId();
        System.out.println("id : " + editid+" - "+item.getTask());
        Boolean checkupdatedata = DB.deleteuserdetails(editid);
    }

    public void createdialog(List<Integer> checkedlist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(thiscontext);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        System.out.println("delete------------");
                        for(int pos:checkedlist){
                            System.out.println("deleting task on - "+pos);
                            deleteItem(pos);
                        }
                        refreshsecondFragment();

                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("do nothing----------------");
//                        refreshsecondFragment();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
