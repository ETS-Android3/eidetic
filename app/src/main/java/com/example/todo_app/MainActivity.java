package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ToDoAdapter taskAdapter;
    private List<ToDoModel> taskList;
    DatabaseHelper DB;
    Context thiscontext;
    private long pressedTime;
    private final String CHANNEL_ID = "Creation_todo";
    private final int NOTIFICATION_ID = 1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        taskList = new ArrayList<>();
        FirstFragment hometab = new FirstFragment();
        SecondFragment todostab = new SecondFragment();
        ThirdFragment notificationtab = new ThirdFragment();
        fourthFragment profiletab = new fourthFragment();
        DB = new DatabaseHelper(this);
        activate_noti();
        createNotificationChannel();
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




        Button save = bottomSheetDialog.findViewById(R.id.newtaskbtn);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                EditText text = bottomSheetDialog.findViewById(R.id.newtaskText);
                EditText datepicker = bottomSheetDialog.findViewById(R.id.datepicker);
                String task = text.getText().toString();
                String date = datepicker.getText().toString();
                System.out.println(task);
                if (task.equals("")) {
                    Toast.makeText(MainActivity.this, "Empty todo entry found.", Toast.LENGTH_SHORT).show();
                } else {
                    if (date.equals("")) {
                        date = getCurDate();
                    }

                    Boolean checkinsertdata = DB.insertuserdetails(task, date);
                    if (checkinsertdata) {
                        Toast.makeText(MainActivity.this, "new entry inserted", Toast.LENGTH_SHORT).show();
                        if (get_noti(1)) {
                            displayNotification(task);
                        }
                        SecondFragment todostab = new SecondFragment();
                        loadFragment(todostab);
                        bottomSheetDialog.cancel();

                    } else {
                        Toast.makeText(MainActivity.this, "entry not inserted", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        bottomSheetDialog.show();
    }

    private void displayNotification(String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("New Todo Created")
                .setContentText(body)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "simple Notification";
            String description = "include All notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCurDate() {
        Calendar c = Calendar.getInstance();
        String date = makeDoubledigit(c.get(Calendar.DAY_OF_MONTH)) + "/" + makeDoubledigit(c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR) +
                "\n" + makeDoubledigit(c.get(Calendar.HOUR_OF_DAY)) + ":" + makeDoubledigit(c.get(Calendar.MINUTE));
        return date;

    }

    public String makeDoubledigit(int a) {
        if (a < 10) {
            return "0" + a;
        } else {
            return "" + a;
        }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void activate_noti() {
        Cursor res = DB.getNotidata();
        if (res.getCount() == 0) {
            DB.insertNotidetails();
        }
    }

    private boolean get_noti(int switch_id) {
        Cursor res = DB.getNotidata();
        int data = 0;
        while (res.moveToNext()) {
            data = res.getInt(switch_id);
        }
        return data == 1;
    }


}
