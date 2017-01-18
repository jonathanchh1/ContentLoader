package com.example.jonat.historyclasscontentprovider.Services;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.jonat.historyclasscontentprovider.Items;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jonat on 1/17/2017.
 */

public class ApiClient extends AsyncTask<String, Void, List<Items>> {
    public static final String BASE_URL = "http://stacktips.com/";


    @Override
    protected List<Items> doInBackground(String... params) {

        if(params.length == 0) {
            return null;
        }

        String posts = params[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        StacktipAPI stacktipAPI = retrofit.create(StacktipAPI.class);
        Call<Stacktip> itemsCall = stacktipAPI.apiLoader(posts);

        try{
            Response<Stacktip>  response = itemsCall.execute();
            Stacktip stacktip = response.body();
            return stacktip.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Items> itemses) {
        if(itemses != null){

        }
    }
}
