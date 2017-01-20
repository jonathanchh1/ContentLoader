package com.example.jonat.historyclasscontentprovider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jonat.historyclasscontentprovider.Services.Stacktip;
import com.example.jonat.historyclasscontentprovider.Services.StacktipAPI;
import com.example.jonat.historyclasscontentprovider.data.ContentContract;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jonat on 1/17/2017.
 */

public class ContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ItemsAdapter itemsAdapter;
    private GridView mGridView;

    private static final int CURSOR_LOADER_ID = 0;
    //static value for our items
    private String FEED_URL = "http://stacktips.com/?json=get_recent_posts&count=45";

    ArrayList<Items> itemsList;
     Items objectItems;
   /**
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

    **/

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
            new DownloadTask().execute(FEED_URL);
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
        //initialize mGridview to the gridView in the fragment_main.xml

        objectItems = new Items();

        itemsList = new ArrayList<>();

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new DownloadTask().execute(FEED_URL);

    }

    public void insertData() {

        ContentValues values = new ContentValues();
        values.put(ContentContract.ContentEntry.COLUMN_ICON, objectItems.getImage());
        values.put(ContentContract.ContentEntry.COLUMN_DESCRIPTION, objectItems.getDescription());
        values.put(ContentContract.ContentEntry.COLUMN_VERSION_NAME, objectItems.getTitle());

        /**
        ContentValues[] items = new ContentValues[flavors.length];
        //Loop through static array of flavors, add each to an instance
        //in the array of ContentValues
        for(int i = 0; i < flavors.length; i++){
            items[i] = new ContentValues();
            items[i].put(ContentContract.ContentEntry.COLUMN_ICON, flavors[i].image);
            items[i].put(ContentContract.ContentEntry.COLUMN_VERSION_NAME, flavors[i].name);
            items[i].put(ContentContract.ContentEntry.COLUMN_DESCRIPTION, flavors[i].description);
        }
**/
        //bulkInsert out ContentValues.
        getActivity().getContentResolver().bulkInsert(ContentContract.ContentEntry.CONTENT_URI,
                new ContentValues[]{values});
    }
    public class DownloadTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


            if (result == 1) {
                itemsAdapter = new ItemsAdapter( getActivity(),  null, 0, CURSOR_LOADER_ID);
                mGridView.setAdapter(itemsAdapter);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            itemsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                Items item = new Items();
                item.setTitle(post.optString("title"));
                item.setImage(post.optString("thumbnail"));
                itemsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        if(data.getCount() != 0){
            itemsList.addAll(Utility.returnListFromCursor(data));

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        clearDataSet();
    }


    private void clearDataSet() {
        if (itemsList != null) {
            itemsList.clear();
            itemsAdapter.notifyDataSetChanged();
        }
    }

}
