package com.cst2335.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.fragment.app.Fragment;

public class MyOpener extends SQLiteOpenHelper {
    public static final String FILENAME = "eventInfoDB";
    public static int VERSION = 1;
    public static final String TABLE_NAME = "eventRecord";
    public static final String COL_ID = "_id";
    public static final String COL_Favorite = "isFavorite";
    public static final String COL_EventName = "EventName";
    public static final String COL_StartDate = "StartDate";
    public static final String COL_MIN_Price = "MIN_Price";
    public static final String COL_MAX_Price = "MAX_Price";
    public static final String COL_URL= "URL_to_ticketmaster";
    public static final String COL_IMG= "IMG_url";
    public static final String FAVORITE_TABLE_NAME = "favoriteRecord";

    public MyOpener(Context ctx){super(ctx, FILENAME, null, VERSION);}

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(SQLiteDatabase db){
        String theEvent = String.format(" %s %s %s %s %s %s %s %s", "id" , "isFavorite", "EventName",
                "StartDate", "MinPrice", "MinPrice", "ticketMasterURL","IMG_url");
        db.execSQL( String.format( "Create table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s  TEXT, %s  TEXT, %s REAL, %s REAL, %s TEXT,%s TEXT);"
                , TABLE_NAME, COL_ID, COL_EventName, COL_StartDate, COL_MIN_Price,
                        "%s  INT,  %s  TEXT, %s  TEXT, %s REAL, %s REAL, %s TEXT,%s TEXT);"
                , TABLE_NAME, COL_ID, COL_Favorite,COL_EventName, COL_StartDate,COL_MIN_Price,

                COL_MAX_Price, COL_URL,COL_IMG ) );
        db.execSQL( String.format( "Create table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s  TEXT, %s  TEXT, %s REAL, %s REAL, %s TEXT,%s TEXT);"
                , FAVORITE_TABLE_NAME, COL_ID, COL_EventName, COL_StartDate, COL_MIN_Price,
                COL_MAX_Price, COL_URL,COL_IMG ) );


    }

    // delete current table, create a new one for a new version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("Drop table if exists " + TABLE_NAME);
        db.execSQL("Drop table if exists " + FAVORITE_TABLE_NAME);
        this.onCreate(db);
    }

}
