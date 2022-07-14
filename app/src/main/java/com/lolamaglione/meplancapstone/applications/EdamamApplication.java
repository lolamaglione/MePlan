package com.lolamaglione.meplancapstone.applications;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.facebook.stetho.Stetho;
import com.lolamaglione.meplancapstone.EdamamClient;
import com.lolamaglione.meplancapstone.MyDatabase;

/**
 * DEMO
 */
public class EdamamApplication extends Application {
    private static MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        // when upgrading versions, kill the original tables by using
        // fallbackToDestructiveMigration()


        // use chrome://inspect to inspect your SQL database
        Stetho.initializeWithDefaults(this);
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}
