package com.hackzero.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String dbname = "task_db";
    private static int version = 1;

    //constructor
    public DatabaseHelper(Context context) {
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE TASKS (_id INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, STATUS TEXT, PRIORITY TEXT)";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    public void updateData(String conditionalParam, String param, String updatedValue, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(param, updatedValue);

        database.update("TASKS", contentValues, "TITLE=?", new String[]{conditionalParam});

    }

    public void insertData(String title, String status, String priority, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("TITLE", title);
        values.put("STATUS", status);
        values.put("PRIORITY", priority);
        database.insert("TASKS", null, values);
    }

    public void deleteData(String param, SQLiteDatabase database) {
        database.delete("TASKS", "TITLE=?", new String[]{param});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
