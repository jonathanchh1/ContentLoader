package com.example.jonat.historyclasscontentprovider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.jonat.historyclasscontentprovider.data.ContentContract;

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
        //initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment_main layout
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //initialize our ImageAdapter
        itemsAdapter = new ItemsAdapter(getActivity(), null, 0, CURSOR_LOADER_ID);
        //initialize mGridview to the gridView in the fragment_main.xml

        mGridView = (GridView) rootView.findViewById(R.id.flavors_grid);

        mGridView.setAdapter(itemsAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //increment the position to match Database ids indexed starting at 1
                int uriId = position + 1;
                //append Id to uri

                Uri uri = ContentUris.withAppendedId(ContentContract.ContentEntry.CONTENT_URI,
                        uriId);

                //create fragment
                DetailFragment detailFragment = DetailFragment.newInstance(uriId, uri);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null).commit();


            }
        });

        return rootView;
    }

    public void insertData() {
        ContentValues[] items = new ContentValues[flavors.length];
        //Loop through static array of flavors, add each to an instance
        //in the array of ContentValues
        for(int i = 0; i < flavors.length; i++){
            items[i] = new ContentValues();
            items[i].put(ContentContract.ContentEntry.COLUMN_ICON, flavors[i].image);
            items[i].put(ContentContract.ContentEntry.COLUMN_VERSION_NAME, flavors[i].name);
            items[i].put(ContentContract.ContentEntry.COLUMN_DESCRIPTION, flavors[i].description);
        }

        //bulkInsert out ContentValues.
        getActivity().getContentResolver().bulkInsert(ContentContract.ContentEntry.CONTENT_URI,
                items);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //Attach loader to our items database query
    //run when loader is initialized.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                ContentContract.ContentEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    //Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        itemsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        itemsAdapter.swapCursor(null);
    }
}
