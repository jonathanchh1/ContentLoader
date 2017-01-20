package com.example.jonat.historyclasscontentprovider;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonat.historyclasscontentprovider.data.ContentContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonat on 1/17/2017.
 */
public class ItemsAdapter extends CursorAdapter {

    private static final String LOG_TAG = ItemsAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;
    private List<Items> itemsList = new ArrayList<Items>();




    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView textView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.flavor_image);
            textView = (TextView) view.findViewById(R.id.flavor_text);
        }
    }

    public ItemsAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "FlavAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    public void updateList(List<Items> mGridData) {
        this.itemsList = mGridData;
        notifyDataSetChanged();
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        int layoutId = R.layout.content_items;

        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In bind View");

        int versionIndex = cursor.getColumnIndex(ContentContract.ContentEntry.COLUMN_VERSION_NAME);
        final String versionName = cursor.getString(versionIndex);
        viewHolder.textView.setText(versionName);

        int imageIndex = cursor.getColumnIndex(ContentContract.ContentEntry.COLUMN_ICON);
        int image = cursor.getInt(imageIndex);
        Log.i(LOG_TAG, "Image reference extracted: " + image);

        viewHolder.imageView.setImageResource(image);

    }

}