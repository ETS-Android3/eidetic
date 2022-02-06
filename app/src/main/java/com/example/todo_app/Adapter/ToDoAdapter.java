package com.example.todo_app.Adapter;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo_app.Utils.DatabaseHelper;
import com.example.todo_app.Model.ToDoModel;
import com.example.todo_app.R;
import com.example.todo_app.SecondFragment;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private SecondFragment activity;
    DatabaseHelper DB;
    private final String CHANNEL_ID = "done_todo";
    private final int NOTIFICATION_ID = 2;


    public ToDoAdapter(SecondFragment activity) {
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tast_layout, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        createNotificationChannel();
        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.date.setText(item.getDate());
        System.out.println("date....."+item.getDate());

        holder.task.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if(checked){
                    int c_id=item.getId();
                    DB = new DatabaseHelper(activity.getContext());
                    DB.updatestatus(c_id, item.getTask(),1);
                    createdialog(c_id);

                }
                if(!checked){
                    int c_id=item.getId();
                    DB = new DatabaseHelper(activity.getContext());
                    DB.updatestatus(c_id, item.getTask(),0);
                }
            }
        });

    }

    public void createdialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getContext());
        builder.setTitle("Delete Task");
        builder.setMessage("You completed the task. Do you want to delete it ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean checkupdatedata = DB.deleteuserdetails(id);
                        if(get_noti(2,DB)){
                            displayNotification("Task completed and removed.");
                        }
                        refreshsecondFragment();

                    }
                });
        builder.setNegativeButton("Keep",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("do nothing----------------");
                        if(get_noti(2,DB)){
                            displayNotification("Task completed.");
                        }

//                        refreshsecondFragment();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void refreshsecondFragment() {
        SecondFragment fragment = new SecondFragment();
        FragmentTransaction transaction = activity.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }
    private void displayNotification(String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity.getActivity(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Task Completed and keeped.")
                .setContentText(body)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity.getActivity());

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
            NotificationManager notificationManager = activity.getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean get_noti(int switch_id,DatabaseHelper DB) {
        Cursor res = DB.getNotidata();
        int data = 0;
        while (res.moveToNext()) {
            data = res.getInt(switch_id);
        }
        return data == 1;
    }

    public int getItemCount() {
        return todoList.size();
    }

    private Boolean toBoolean(int n) {
        return n != 0;
    }

    public SecondFragment getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView date;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckbox);
            date = view.findViewById(R.id.todoDate);

        }
    }


}

