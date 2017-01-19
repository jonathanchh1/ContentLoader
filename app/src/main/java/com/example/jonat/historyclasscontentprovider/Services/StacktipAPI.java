package com.example.jonat.historyclasscontentprovider.Services;

import com.example.jonat.historyclasscontentprovider.Items;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jonat on 1/17/2017.
 */

public interface StacktipAPI {
    @GET("?json=get_recent_posts&count=50#posts/0/")
    Call<Stacktip> apiLoader(@Query("attachment") String posts);
}
