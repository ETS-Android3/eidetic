package com.example.todo_app.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table userdetails(id INTEGER PRIMARY KEY AUTOINCREMENT,task TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {

        DB.execSQL("drop Table if exists Userdetails");
    }

    public Boolean insertuserdetails(String task) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("task", task);
        long result = DB.insert("Userdetails", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateuserdetails(int id, String task) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("task", task);
        Cursor cursor = DB.rawQuery("select * from Userdetails where id=?", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {

            long result = DB.update("Userdetails", cv, "id=?", new String[]{String.valueOf(id)});
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

        Cursor cursor = DB.rawQuery("select * from Userdetails where id=?", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {

            long result = DB.delete("Userdetails", "id=?", new String[]{String.valueOf(id)});
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

        Cursor cursor = DB.rawQuery("select * from Userdetails",null);

        return cursor;
    }




}
