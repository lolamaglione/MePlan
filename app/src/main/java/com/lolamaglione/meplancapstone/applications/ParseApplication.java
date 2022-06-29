package com.lolamaglione.meplancapstone.applications;

import android.app.Application;

import androidx.room.Room;

import com.lolamaglione.meplancapstone.MyDatabase;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * This enables the code to communicate with ParseApplication in order to create
 * new users and keep track of current users.
 */
public class ParseApplication extends Application {
    private static MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(RecipeController.class);
        ParseObject.registerSubclass(ScheduleController.class);
        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gjRe9IDq8FL3mE1yqExgQe9hVdWFOFqvyOH5A0wt")
                .clientKey("o7VqwkTpcAu4PHpHbq6EPVIlPwZdg5MeqMxNbKTU")
                .server("https://parseapi.back4app.com")
                .build()
        );

        myDatabase = Room.databaseBuilder(this, MyDatabase.class, MyDatabase.NAME).fallbackToDestructiveMigration().build();

    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}
