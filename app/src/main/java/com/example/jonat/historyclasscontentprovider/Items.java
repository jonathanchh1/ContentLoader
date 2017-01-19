package com.example.jonat.historyclasscontentprovider;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jonat on 1/17/2017.
 */

public class Items implements Parcelable {
    @SerializedName("title")
   private String title;

    @SerializedName("description")
   private String description;

    @SerializedName("url")
    private String image;

    protected Items(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
    }
}
