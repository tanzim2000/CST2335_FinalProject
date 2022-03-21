package com.cst2335.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {
    public static final String FILENAME = "eventTicketDB";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "eventRecord";
    public static final String COL_ID = "_id";
    public static final String COL_EventName = "EventName";
    public static final String COL_StartDate = "StartDate";
    public static final double COL_MIN_Price = 0.00;
    public static final double COL_MAX_Price = 0.00;
    public static final String COL_URL= "https://www.ticketmaster.ca/";
    public static final String COL_IMG= "https://www.ticketmaster.ca/";


    public MyOpener(Context ctx){super(ctx, FILENAME, null,VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db){
        String theEvent = String.format(" %s %s %s %s %s %s %s",
                "id" , "EventName", "StartDate" , "0.00", "0.00"," ", " ");
        db.execSQL( String.format( "Create table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s TEXT, %s text, %s text, %s TEXT, % TEXT);",
                        TABLE_NAME, COL_ID,  COL_EventName,COL_StartDate,
                        COL_MIN_Price, COL_MAX_Price,COL_URL,COL_IMG) );

    }

    // delete current table, create a new one for a new version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("Drop table if exists " + TABLE_NAME);
        this.onCreate(db);
    }

}
