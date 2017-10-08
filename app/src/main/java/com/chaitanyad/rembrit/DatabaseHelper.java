package com.chaitanyad.rembrit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chait on 8/27/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "rembrit.db";
    public static final String TABLE_NAME = "reminders";
    public static final String TABLE_NAME2 = "routine";
    public static final String _ID = "_id";
    public static final String COL2 = "rem_text";
    public static final String COL3 = "rem_time_milli";
    public static final String COL4 = "rem_time";
    public static final String _ID2 = "_id";

    public static final String COL2_2_H = "wakeupHour";
    public static final String COL2_2_M = "wakeupMin";

    public static final String COL3_2_H = "leaveforWorkHour";
    public static final String COL3_2_M = "leaveforWorkMin";


    public static final String COL4_2_H = "reachWorkHour";
    public static final String COL4_2_M = "reachWorkMin";

    public static final String COL5_2_H = "lunchHour";
    public static final String COL5_2_M = "lunchMin";

    public static final String COL6_2_H = "leaveFromWorkHour";
    public static final String COL6_2_M = "leaveFromWorkMin";

    public static final String COL7_2_H = "reachHomeHour";
    public static final String COL7_2_M = "reachHomeMin";


    public static final String COL8_2_H = "dinnerHour";
    public static final String COL8_2_M = "dinnerMin";

    public static final String COL9_2_H = "sleepHour";
    public static final String COL9_2_M = "sleepMin";



    public static final String[] ALL_KEYS = new String[]{_ID, COL2, COL3, COL4};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT)");
        db.execSQL("create table " + TABLE_NAME2 + " (" + _ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL2_2_H + " TEXT, "+COL2_2_M+" TEXT," + COL3_2_H + " TEXT, "+COL3_2_M+" TEXT," + COL4_2_H + " TEXT, "+COL4_2_M+" TEXT,"+COL5_2_H+" TEXT, "+COL5_2_M+" TEXT,"+COL6_2_H+" TEXT, "+COL6_2_M+" TEXT,"+COL7_2_H+" TEXT, "+COL7_2_M+" TEXT, "+COL8_2_H+" TEXT, "+COL8_2_M+" TEXT,"+COL9_2_H+" TEXT, "+COL9_2_M+" TEXT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    public boolean insertData(String rem_text, long rem_timeinmilli) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, rem_text);
        contentValues.put(COL3, rem_timeinmilli);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(rem_timeinmilli));
        SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm a");
        String dateString2 = formatter2.format(new Date(rem_timeinmilli));
        dateString = dateString + " at " + dateString2;
        contentValues.put(COL4, dateString);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData_routine(String wakeupHour,String wakeupMin,String leaveForWorkHour,String leaveForWorkMin,String reachWorkHour,String reachWorkMin,String lunchHour,String lunchMin,String leaveFromWorkHour,String leaveFromWorkMin,String reachHomeHour,String reachHomeMin,String dinnerHour,String dinnerMin,String sleepHour,String sleepMin) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME2+";");
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_2_H,wakeupHour);
        contentValues.put(COL2_2_M,wakeupMin);
        contentValues.put(COL3_2_H,leaveForWorkHour);
        contentValues.put(COL3_2_M,leaveForWorkMin);
        contentValues.put(COL4_2_H,reachWorkHour);
        contentValues.put(COL4_2_M,reachWorkMin);
        contentValues.put(COL5_2_H,lunchHour);
        contentValues.put(COL5_2_M,lunchMin);
        contentValues.put(COL6_2_H,leaveFromWorkHour);
        contentValues.put(COL6_2_M,leaveFromWorkMin);
        contentValues.put(COL7_2_H,reachHomeHour);
        contentValues.put(COL7_2_M,reachHomeMin);
        contentValues.put(COL8_2_H,dinnerHour);
        contentValues.put(COL8_2_M,dinnerMin);
        contentValues.put(COL9_2_H,sleepHour);
        contentValues.put(COL9_2_M,sleepMin);

        long result = db.insert(TABLE_NAME2, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getMaxId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT MAX( _id) from " + TABLE_NAME + ";", null);
        return res;
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

;
    public Cursor getAllRows() { //Reminder table
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + TABLE_NAME + " ORDER BY " + _ID + " DESC;", null);
        return res;
    }

    public Cursor getAllRows_routine(String hour,String min) { //Routine table
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT "+hour+","+min+" from " + TABLE_NAME2 + ";", null);
        return res;
    }

    public void deleteRow(String delId) {
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + _ID + "=" + delId);
        }

    }
}
