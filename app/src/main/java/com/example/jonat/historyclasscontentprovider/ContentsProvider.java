package com.example.jonat.historyclasscontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jonat on 1/17/2017.
 */

public class ContentsProvider extends ContentProvider {
    private static final String LOG_TAG = ContentsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ContentDBHelper mOpenHelper;


    //Codes for UriMatcher//
    private static final int CONTENT = 100;
    private static final int CONTENT_ID = 200;


    private static UriMatcher buildUriMatcher() {
        //Build a UriMatcher by adding a specific code to return based
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContentContract.CONTENT_AUTHORITY;

        //add a code for each type of URI you want
        matcher.addURI(authority, ContentContract.ContentEntry.TABLE_CONTENT, CONTENT);
        matcher.addURI(authority, ContentContract.ContentEntry.TABLE_CONTENT + "/#", CONTENT_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            //All content selected

            case CONTENT:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ContentContract.ContentEntry.TABLE_CONTENT,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                return retCursor;
            }

            //individual content based on Id selected
            case CONTENT_ID:{
                    retCursor = mOpenHelper.getReadableDatabase().query(
                            ContentContract.ContentEntry.TABLE_CONTENT,
                            projection,
                            ContentContract.ContentEntry._ID + " = ?",
                            new String[]{String.valueOf(ContentUris.parseId(uri))},
                            null,
                            null,
                            sortOrder);
                    return retCursor;
                }
                default:
                    //By default, we assume a bad URI
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case CONTENT:{
                return ContentContract.ContentEntry.CONTENT_DIR_TYPE;
            }
            case CONTENT_ID:{
                return ContentContract.ContentEntry.CONTENT_ITEM_TYPE;
            }

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)){
            case CONTENT: {
                long _Id = db.insert(ContentContract.ContentEntry.TABLE_CONTENT, null, values);
                //insert unless it is already contained in the database
                if(_Id > 0){
                    returnUri = ContentContract.ContentEntry.buildFlavorsUri(_Id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into:" + uri);
                }
                break;
            }

            default:{
                throw new UnsupportedOperationException("Unknown uri:" + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case CONTENT:
                numDeleted = db.delete(
                        ContentContract.ContentEntry.TABLE_CONTENT, selection, selectionArgs);
                //reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        ContentContract.ContentEntry.CONTENT_ITEM_TYPE + "'");
                break;
            case CONTENT_ID:
                numDeleted = db.delete(ContentContract.ContentEntry.TABLE_CONTENT,
                        ContentContract.ContentEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                //reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        ContentContract.ContentEntry.TABLE_CONTENT + "'");

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTENT:
                db.beginTransaction();
                //keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(ContentContract.ContentEntry.TABLE_CONTENT,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            ContentContract.ContentEntry.COLUMN_VERSION_NAME) + " but value is already in database. ");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        //if no errors, declare a successful transaction;
                        //database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    //if theere was successfull insertion,  notify the content  resolver that there
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;

            default:
                return super.bulkInsert(uri, values);
        }

    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if(values == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }
        switch (sUriMatcher.match(uri)){
            case CONTENT:{
                numUpdated = db.update(ContentContract.ContentEntry.TABLE_CONTENT,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case CONTENT_ID: {
                numUpdated = db.update(ContentContract.ContentEntry.TABLE_CONTENT,
                        values,
                        ContentContract.ContentEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if(numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
