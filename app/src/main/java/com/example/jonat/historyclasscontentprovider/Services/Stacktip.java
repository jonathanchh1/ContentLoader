package com.example.jonat.historyclasscontentprovider.Services;

import com.example.jonat.historyclasscontentprovider.Items;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonat on 1/17/2017.
 */

public class Stacktip {
    @SerializedName("posts")

    private List<Items> items = new ArrayList<>();

    public List<Items> getItems() {
        return items;
    }
}
