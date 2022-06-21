package com.lolamaglione.meplancapstone;

import android.content.Context;

import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class EdamamClient {

    public static final String REST_URL = "https://api.edamam.com";
    public static final String REST_APP_ID = "49c4a7b7";
    public static final String REST_APP_KEY = "27ea06df50c6b95aa310fadc03f4faf2";
    public AsyncHttpClient client = new AsyncHttpClient();


    public void getRecipeFeed(JsonHttpResponseHandler handler, String q, int page){
        String apiUrl = "https://api.edamam.com/api/recipes/v2";

        RequestParams params = new RequestParams();
        params.put("app_id", REST_APP_ID);
        params.put("app_key", REST_APP_KEY);
        params.put("type", "public");
        params.put("q", q);
        //params.put("q", "tomato, chicken, garlic");
        //params.put("cuisineType", null);
        client.get(apiUrl, params, handler);
    }

    public AsyncHttpClient getClient(){
        return client;
    }

    private String getApiUrl(String s) {
        return REST_URL + s;
    }

}