package com.reminder.three;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by arbalestx on 13/2/16.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    public static final String TABLE_TASKS = "tasklist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";

    private static final String DATABASE_NAME = "task.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TASKS + "(" + COLUMN_ID
            + " integer primary key autoincrement," + COLUMN_TASK
            + " text," + COLUMN_DATE
            + " DATETIME," + COLUMN_LAT
            + " double," + COLUMN_LNG + " double)";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
}
