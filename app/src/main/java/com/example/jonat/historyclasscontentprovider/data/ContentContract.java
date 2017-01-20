package com.example.jonat.historyclasscontentprovider.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jonat on 1/17/2017.
 */

public class ContentContract {
    public static final String CONTENT_AUTHORITY = "com.example.jonat.historyclasscontentprovider.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class  ContentEntry implements BaseColumns{
        //table name
        public static final String TABLE_CONTENT = "content";
        //columns

        public static final String _ID = "_id";
        public static final String COLUMN_ICON = "thumbnail";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VERSION_NAME = "title";

        //create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_CONTENT).build();
        //create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_CONTENT;
        //create cursor of base type for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_CONTENT;

        //for building URIs on insertion
        public static Uri buildFlavorsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
