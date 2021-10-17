package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo_app.Adapter.ToDoAdapter;
import com.example.todo_app.Model.ToDoModel;
import com.example.todo_app.Utils.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ToDoAdapter taskAdapter;
    private List<ToDoModel> taskList;
    DatabaseHelper DB;
    Context thiscontext;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        taskList = new ArrayList<>();
        FirstFragment hometab = new FirstFragment();
        SecondFragment todostab = new SecondFragment();
        ThirdFragment notificationtab = new ThirdFragment();
        fourthFragment profiletab = new fourthFragment();

        loadFragment(todostab);
        View bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNevigationView);
        bottomNavigationView.setBackground(null);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNevigationView);
        bottomNav.setSelectedItemId(R.id.library);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        loadFragment(hometab);
                        return true;
                    case R.id.library:
                        loadFragment(todostab);
                        return true;
                    case R.id.notify:
                        loadFragment(notificationtab);
                        return true;
                    case R.id.account:
                        loadFragment(profiletab);
                        return true;
                }
                return false;
            }
        });

        FloatingActionButton addfloatBotton = (FloatingActionButton) findViewById(R.id.addtaskbtn);
        addfloatBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });


    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.DialogStyle);
        bottomSheetDialog.setContentView(R.layout.new_task);
        thiscontext = bottomSheetDialog.getContext();

        DB = new DatabaseHelper(thiscontext);

        Button save = bottomSheetDialog.findViewById(R.id.newtaskbtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = bottomSheetDialog.findViewById(R.id.newtaskText);
                String task = text.getText().toString();
                System.out.println(task);
                if (task.equals("")) {
                    Toast.makeText(MainActivity.this, "Empty todo entry found.", Toast.LENGTH_SHORT).show();
                } else {

                    Boolean checkinsertdata = DB.insertuserdetails(task);
                    if (checkinsertdata) {
                        Toast.makeText(MainActivity.this, "new entry inserted", Toast.LENGTH_SHORT).show();

                        SecondFragment todostab = new SecondFragment();
                        loadFragment(todostab);

                    } else {
                        Toast.makeText(MainActivity.this, "entry not inserted", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


}
