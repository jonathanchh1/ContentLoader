package com.example.jonat.historyclasscontentprovider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonat on 1/16/2017.
 */

public class ProfileAdapter extends ArrayAdapter<ProfileInfo>{

    public ProfileAdapter(Context context, ArrayList<ProfileInfo> profileInfos){
        super(context, 0, profileInfos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get the data item
        ProfileInfo profileInfo = getItem(position);
        //check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.content_items, parent, false);
        }

        TextView Name = (TextView) view.findViewById(R.id.tvName);
        TextView Email = (TextView) view.findViewById(R.id.tvEmail);
        TextView Phone = (TextView) view.findViewById(R.id.tvPhone);
        Name.setText(profileInfo.name);
        Email.setText("");
        Phone.setText("");
        if(profileInfo.emails.size() > 0 && profileInfo.emails.get(0) != null){
            Email.setText(profileInfo.emails.get(0).address);
        }

        if(profileInfo.numbers.size() > 0 && profileInfo.numbers.get(0) != null){
            Phone.setText(profileInfo.numbers.get(0).number);
        }

        return view;

    }
}
