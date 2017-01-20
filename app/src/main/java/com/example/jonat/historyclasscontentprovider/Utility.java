package com.example.jonat.historyclasscontentprovider;

import android.content.ClipData;
import android.database.Cursor;

import com.example.jonat.historyclasscontentprovider.data.ContentContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonat on 1/20/2017.
 */

public class Utility {


    public static List<Items> returnListFromCursor(Cursor cursor) {
        List<Items> rowItemList = new ArrayList<>();
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(ContentContract.ContentEntry.COLUMN_VERSION_NAME));
                String description = cursor.getString(cursor.getColumnIndex(ContentContract.ContentEntry.COLUMN_DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndex(ContentContract.ContentEntry.COLUMN_ICON));
                 Items newsFeed = new Items();
                newsFeed.setTitle(title);
                newsFeed.setImage(image);
                newsFeed.setDescription(description);
                rowItemList.add(newsFeed);
                // do what ever you want here
            } while (cursor.moveToNext());
        }

        return rowItemList;
    }

}
