package com.example.todo_app.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "eideticall.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table Tasklist(id INTEGER PRIMARY KEY AUTOINCREMENT,task TEXT NOT NULL,date TEXT NOT NULL,status INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {

        DB.execSQL("drop Table if exists Tasklist");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Boolean insertuserdetails(String task,String date) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("task", task);
        cv.put("date", date);
        cv.put("status", 0);
        long result = DB.insert("Tasklist", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Boolean updateuserdetails(int id, String task,String date){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("task", task);
        cv.put("date", date);
        Cursor cursor = DB.rawQuery("select * from Tasklist where id=?", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {

            long result = DB.update("Tasklist", cv, "id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Boolean updatestatus(int id, String task,int status) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String date=getCurDate();
        cv.put("task", task);
        cv.put("date", date);
        cv.put("status",status);
        Cursor cursor = DB.rawQuery("select * from Tasklist where id=?", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {

            long result = DB.update("Tasklist", cv, "id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteuserdetails(int id) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("select * from Tasklist where id=?", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {

            long result = DB.delete("Tasklist", "id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getdata() {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("select * from Tasklist",null);

        return cursor;
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
