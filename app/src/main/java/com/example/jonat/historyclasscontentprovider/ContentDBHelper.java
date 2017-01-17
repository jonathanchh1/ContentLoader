package com.example.jonat.historyclasscontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jonat on 1/17/2017.
 */

public class ContentDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ContentDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 12;

    public ContentDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create the database

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " +
                ContentContract.ContentEntry.TABLE_CONTENT + "(" +
                ContentContract.ContentEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContentContract.ContentEntry.COLUMN_VERSION_NAME + " TEXT NOT NULL, " +
                ContentContract.ContentEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ContentContract.ContentEntry.COLUMN_ICON + " INTEGER NOT NULL " + ");";


         db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    //Upgrade database when version is changed

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
        + newVersion + ". OLD DATA WILL BE DESTROYED");

        //Drop the table
        db.execSQL("DROP TABLE IF EXISTS" + ContentContract.ContentEntry.TABLE_CONTENT);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
        ContentContract.ContentEntry.TABLE_CONTENT + "'");

        //re-create database
        onCreate(db);
    }
}
