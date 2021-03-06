package com.lolamaglione.meplancapstone;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.lolamaglione.meplancapstone.converters.ListConverter;
import com.lolamaglione.meplancapstone.models.Recipe;
import com.lolamaglione.meplancapstone.models.RecipeDao;
import com.lolamaglione.meplancapstone.models.Schedule;

import java.util.List;

/**
 * This class creates the room sqlite database
 */
@Database(entities={Recipe.class}, version=10)
@TypeConverters({ListConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();

    public static final String NAME = "MY_DATABASE";
}
