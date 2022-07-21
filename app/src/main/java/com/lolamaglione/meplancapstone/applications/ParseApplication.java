package com.lolamaglione.meplancapstone.applications;

import android.app.Application;

import androidx.room.Room;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.lolamaglione.meplancapstone.MyDatabase;
import com.lolamaglione.meplancapstone.ParseFacebookUtils;
import com.lolamaglione.meplancapstone.controllers.IngredientController;
import com.lolamaglione.meplancapstone.controllers.RecipeController;
import com.lolamaglione.meplancapstone.controllers.ScheduleController;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

/**
 * This enables the code to communicate with ParseApplication in order to create
 * new users and keep track of current users.
 */
public class ParseApplication extends Application {
    private static MyDatabase myDatabase;
    private static HashMap<String, SortedMap<Integer, List<Recipe>>> inMemoryResults;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(RecipeController.class);
        ParseObject.registerSubclass(ScheduleController.class);
        ParseObject.registerSubclass(IngredientController.class);
        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gjRe9IDq8FL3mE1yqExgQe9hVdWFOFqvyOH5A0wt")
                .clientKey("o7VqwkTpcAu4PHpHbq6EPVIlPwZdg5MeqMxNbKTU")
                .server("https://parseapi.back4app.com")
                .build()
        );
        ParseFacebookUtils.initialize(this);
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, MyDatabase.NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        inMemoryResults = new HashMap<>();
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }

    public HashMap<String, SortedMap<Integer, List<Recipe>>> getInMemoryResults() {
        return inMemoryResults;
    }

}
