package com.example.jonat.historyclasscontentprovider;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

/**
 * Created by jonat on 1/17/2017.
 */

public class ContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ItemsAdapter itemsAdapter;
    private GridView mGridView;

    private static final int CURSOR_LOADER_ID = 0;
    //static value for our items

    Items[] flavors = {
            new Items("Cupcake", "The first release of Android", R.drawable.cupcake),
            new Items ("Donut", "The world's information is at your fingertips – " +
                    "search the web, get driving directions... or just watch cat videos.",
                    R.drawable.donut),
            new Items("Eclair", "Make your home screen just how you want it. Arrange apps " +
                    "and widgets across multiple screens and in folders. Stunning live wallpapers " +
                    "respond to your touch.", R.drawable.eclair),
            new Items("Froyo", "Voice Typing lets you input text, and Voice Actions let " +
                    "you control your phone, just by speaking.", R.drawable.froyo),
            new Items("GingerBread", "New sensors make Android great for gaming - so " +
                    "you can touch, tap, tilt, and play away.", R.drawable.gingerbread),
            new Items("Honeycomb", "Optimized for tablets, this release opens up new " +
                    "horizons wherever you are.", R.drawable.honeycomb),
            new Items("Ice Cream Sandwich", "Android comes of age with a new, refined design. " +
                    "Simple, beautiful and beyond smart.", R.drawable.icecream),
            new Items("Jelly Bean", "Android is fast and smooth with buttery graphics. " +
                    "With Google Now, you get just the right information at the right time.",
                    R.drawable.jellybean),
            new Items("KitKat", "Smart, simple, and truly yours. A more polished design, " +
                    "improved performance, and new features.", R.drawable.kitkat),
            new Items("Lollipop", "A sweet new take on Android. Get the smarts of Android on" +
                    " screens big and small – with the right information at the right moment.",
                    R.drawable.lollipop)
    };

    public ContentFragment(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Cursor cursor = getActivity().getContentResolver()
                .query(ContentContract.ContentEntry.CONTENT_URI,
                        new String[]{ContentContract.ContentEntry._ID},
                        null,
                        null,
                        null);
        if(cursor.getCount() == 0){
            insertData();
        }
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void insertData() {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;k
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
