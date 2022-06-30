package com.lolamaglione.meplancapstone.models;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lolamaglione.meplancapstone.models.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT Recipe.title FROM Recipe where Recipe.`query` = :this_query")
    List<Recipe> recentItems(String this_query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    static void insertModel(Recipe... recipes) {

    }

}
