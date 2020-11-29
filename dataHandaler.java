package com.example.todo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.Model.Todo_model;

import java.util.ArrayList;
import java.util.List;

public class dataHandaler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "todolistDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE" + TODO_TABLE + "(" + ID +"INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK + "TEXT, " + STATUS + "INTEGER)";
    private SQLiteDatabase db;

    public dataHandaler(Context context) {
        super(context, NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DROP OLDER TABLE
        db.execSQL("DROP TABLE IF EXISTS" + TODO_TABLE);
        //CREATE TABLE
        onCreate(db);

    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(Todo_model task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    public List<Todo_model> getAllTasks() {
        List<Todo_model> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        Todo_model task = new Todo_model();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                    } while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return taskList;
    }
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        db.update(TODO_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }
    public void updateTask(int id,String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv,ID +"=?", new String[] {String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE,ID+"=?",new String[]{String.valueOf(id)});
    }

}