package com.reminder.three;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by arbalestx on 15/2/16.
 */
public class TasksDataSource {
    private SQLiteDatabase database;
    private SqliteHelper dbHelper;
    private String[] allColumns = { SqliteHelper.COLUMN_ID,
            SqliteHelper.COLUMN_TASK, SqliteHelper.COLUMN_DATE,
            SqliteHelper.COLUMN_LAT, SqliteHelper.COLUMN_LNG};
    private Context mCtx;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    public TasksDataSource(Context context) {
        mCtx = context;
    }

    public TasksDataSource open() throws SQLException {
        dbHelper = new SqliteHelper(mCtx);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public boolean createTask(String task, Calendar dateTime, double lat, double lng) {
        dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String strDateTime = dateFormat.format(dateTime.getTime());

        values.put(SqliteHelper.COLUMN_TASK, task);
        values.put(SqliteHelper.COLUMN_DATE, strDateTime);
        //values.put(SqliteHelper.COLUMN_LAT, lat);
        //values.put(SqliteHelper.COLUMN_LNG, lng);

        long insertId = database.insert(SqliteHelper.TABLE_TASKS, null,
                values);
        Cursor cursor = database.query(SqliteHelper.TABLE_TASKS,
                allColumns, SqliteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
//        Tasks newTask = cursorToTask(cursor);
        cursor.close();
        return true;
    }

    private Tasks cursorToTask(Cursor cursor) {
        Tasks task = new Tasks();
        task.setId(cursor.getLong(0));
        task.setTask(cursor.getString(1));
        try {
            task.setDateTime(dateFormat.parse(cursor.getString(2)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //task.setLat(cursor.getDouble(3));
        //task.setLng(cursor.getDouble(4));
        return task;
    }

    public void deleteTask(Tasks task) {
        long id = task.getId();
        System.out.println("Task deleted with id: " + id);
        database.delete(SqliteHelper.TABLE_TASKS, SqliteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Tasks> getAllTasks() {
        List<Tasks> tasks = new ArrayList<Tasks>();

        Cursor cursor = database.query(SqliteHelper.TABLE_TASKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tasks task = null;
            task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tasks;
    }

    public Tasks retrieveTask(long id) {
        Cursor c = database.query(SqliteHelper.TABLE_TASKS,
                allColumns, "_id=" + id, null, null, null, null);
        c.moveToFirst();
        return cursorToTask(c);
    }

    public void updateTasks(long id, String task, Calendar dateTime, double lat, double lng) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String strDateTime = dateFormat.format(dateTime.getTime());
        values.put(SqliteHelper.COLUMN_TASK, task);
        values.put(SqliteHelper.COLUMN_DATE, strDateTime);

        db.update(SqliteHelper.TABLE_TASKS, values, "_id=?", new String[] {String.valueOf(id)});
    }
}
