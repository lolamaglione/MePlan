package com.lolamaglione.meplancapstone;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

/**
 * this class enables us to communicate EDMAME with the API
 */
public class EdamamClient {

    public static final String REST_URL = "https://api.edamam.com";
    public static final String REST_APP_ID = "49c4a7b7";
    public static final String REST_APP_KEY = "27ea06df50c6b95aa310fadc03f4faf2";
    public AsyncHttpClient client;
    public static final String TAG = "client";

    public EdamamClient(){
        client = new AsyncHttpClient();
    }


    public void getRecipeFeed(JsonHttpResponseHandler handler, String q, int page, String nextPage){
        if(client == null){
            client = new AsyncHttpClient();
        }
        String apiUrl = "https://api.edamam.com/api/recipes/v2";
        if (page != 0){
            apiUrl = nextPage;
        }
        RequestParams params = new RequestParams();
        params.put("app_id", REST_APP_ID);
        params.put("app_key", REST_APP_KEY);
        params.put("type", "public");
        params.put("q", q);
        Log.i(TAG, "url: " + apiUrl + " q: " + q);
        client.get(apiUrl, params, handler);
    }

    public void getRecipeFeed(JsonHttpResponseHandler handler, String q, int page, String nextPage, String cuisine){
        if(client == null){
            client = new AsyncHttpClient();
        }
        String apiUrl = "https://api.edamam.com/api/recipes/v2";
        if (page != 0){
            apiUrl = nextPage;
        }
        RequestParams params = new RequestParams();
        params.put("app_id", REST_APP_ID);
        params.put("app_key", REST_APP_KEY);
        params.put("type", "public");
        params.put("q", q);
        //params.put("q", "tomato, chicken, garlic");
        if (!cuisine.equals(" ")){
            params.put("cuisineType", cuisine);
        }
        Log.i(TAG, "url: " + apiUrl + " q: " + q);
        client.get(apiUrl, params, handler);
    }

    public AsyncHttpClient getClient(){
        return client;
    }

    private String getApiUrl(String s) {
        return REST_URL + s;
    }

}
