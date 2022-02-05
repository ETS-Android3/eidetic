package com.example.todo_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.Adapter.ToDoAdapter;
import com.example.todo_app.Model.ToDoModel;
import com.example.todo_app.Utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter adapter;
    Context thiscontext;
    DatabaseHelper DB;
    private List<ToDoModel> taskList;
    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target){
        return false;
    }


    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder,int direction){
        final int position=viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getContext().thiscontext);
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            System.out.println("delete------------"+position);
                            gettaskdone("delete",position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("do nothing----------------");
                            refreshsecondFragment();
                        }
                    });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        else{
            gettaskdone("edit",position);
        }


    }
    @Override
    public void onChildDraw(Canvas c,RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,float dx,float dy,int actionState,boolean isCurrentlyactive){
        super.onChildDraw(c,recyclerView,viewHolder,dx/4,dy,actionState,isCurrentlyactive);
        // for limit on swipe make dx to dx/4 on super.onChildDraw, for full swipe set dx
        Drawable icon;
        ColorDrawable background;
        View itemView= viewHolder.itemView;
        int backgroundcorneroffset=20;
        if(dx>0){
            icon= ContextCompat.getDrawable(adapter.getContext().thiscontext,R.drawable.ic_edit);
            background=new ColorDrawable(ContextCompat.getColor(adapter.getContext().thiscontext,R.color.blue));
        }
        else {
            icon= ContextCompat.getDrawable(adapter.getContext().thiscontext,R.drawable.ic_delete);
            background=new ColorDrawable(Color.RED);
        }

        int iconMargin=(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop=itemView.getTop()+(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconBottom=iconTop+icon.getIntrinsicHeight();

        if(dx>0){
            int iconLeft=itemView.getLeft()+iconMargin;
            int iconRight=itemView.getLeft()+iconMargin+icon.getIntrinsicWidth();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
            background.setBounds(itemView.getLeft(),itemView.getTop(),itemView.getLeft()+((int)dx)+backgroundcorneroffset,itemView.getBottom());
        }
        else if(dx<0){
            int iconLeft=itemView.getRight()-iconMargin-icon.getIntrinsicWidth();
            int iconRight=itemView.getRight()-iconMargin;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
            background.setBounds(itemView.getRight()+((int)dx)-backgroundcorneroffset,itemView.getTop(),itemView.getRight(),itemView.getBottom());
        }
        else{
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }

    public void gettaskdone(String action,int pos){
        taskList = new ArrayList<>();
        thiscontext=adapter.getContext().thiscontext;
        DB = new DatabaseHelper(thiscontext);
        Cursor res = DB.getdata();
        if (res.getCount() == 0) {
            Toast.makeText(thiscontext, "whoohoo! No ToDo Present", Toast.LENGTH_LONG).show();

        } else {

            while (res.moveToNext()) {
                ToDoModel task = new ToDoModel();
                task.setId(res.getInt(0));
                task.setTask(res.getString(1));
                task.setDate(res.getString(2));
                task.setStatus(res.getInt(3));

                taskList.add(task);
                System.out.println(res.getInt(0) + res.getString(1));
            }
        }

        if(action.equals("edit")){
            System.out.println("editing....................");
            editItem(pos);
        }
        else{
            System.out.println("Deleting....................");
            deleteItem(pos);
        }




    }

    public void editItem(int position) {
        ToDoModel item = taskList.get(position);
        int editid=item.getId();
        int editstatus=item.getStatus();
        String edittask=item.getTask();
        String editdate= item.getDate();
        System.out.println("id : "+editid+" task : "+edittask+" date : "+editdate+" status : "+editstatus);
        showBottomSheetDialog(editid,edittask,editdate);
    }

    public void deleteItem(int position) {
        ToDoModel item = taskList.get(position);
        int editid=item.getId();
        String editdate=item.getDate();
        System.out.println("id : "+editid+" date : "+editdate);
        Boolean checkupdatedata=DB.deleteuserdetails(editid);
        if(checkupdatedata){
            Toast.makeText(thiscontext,"Deleted Task",Toast.LENGTH_LONG).show();
            refreshsecondFragment();

        }
        else{
            Toast.makeText(thiscontext,"Task not Deleted",Toast.LENGTH_LONG).show();
        }

    }


    private void showBottomSheetDialog(int id,String prevtask,String prevdate) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(thiscontext,R.style.DialogStyle);
        bottomSheetDialog.setContentView(R.layout.new_task);
        thiscontext = bottomSheetDialog.getContext();

        DB=new DatabaseHelper(thiscontext);
        EditText text = bottomSheetDialog.findViewById(R.id.newtaskText);
        EditText date = bottomSheetDialog.findViewById(R.id.datepicker);
        Button save=bottomSheetDialog.findViewById(R.id.newtaskbtn);
        save.setText("Update");
        text.setText(prevtask);
        date.setText(prevdate);

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                String task=text.getText().toString();
                String newdate=date.getText().toString();
                System.out.println(task);
                if(prevtask.equals(task) && prevdate.equals(newdate)){
                    Toast.makeText(thiscontext,"No update made.",Toast.LENGTH_SHORT).show();
                    refreshsecondFragment();
                    bottomSheetDialog.cancel();
                }
                else {
                    Boolean checkupdatedata=DB.updateuserdetails(id,task,newdate);
                    if(checkupdatedata){
                        Toast.makeText(thiscontext,"update saved",Toast.LENGTH_SHORT).show();
                        refreshsecondFragment();
                        bottomSheetDialog.cancel();

                    }
                    else{
                        Toast.makeText(thiscontext,"update not saved",Toast.LENGTH_SHORT).show();
                        refreshsecondFragment();
                        bottomSheetDialog.cancel();
                    }
                }


            }
        });

        bottomSheetDialog.show();
    }

    private void refreshsecondFragment() {
        SecondFragment fragment = new SecondFragment();
        FragmentTransaction transaction = adapter.getContext().getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }





}
