package com.lolamaglione.meplancapstone.applications;

import android.app.Application;

import com.parse.Parse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * This enables the code to communicate with ParseApplication in order to create
 * new users and keep track of current users.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gjRe9IDq8FL3mE1yqExgQe9hVdWFOFqvyOH5A0wt")
                .clientKey("o7VqwkTpcAu4PHpHbq6EPVIlPwZdg5MeqMxNbKTU")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
