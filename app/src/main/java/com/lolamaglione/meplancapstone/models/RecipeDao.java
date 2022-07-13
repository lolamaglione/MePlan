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

    @Query("SELECT Recipe.* FROM Recipe WHERE Recipe.generalIngredients LIKE :this_query LIMIT 100")
    List<Recipe> recentItems(String this_query);

    @Query("SELECT Recipe.* FROM Recipe WHERE Recipe.`query` = :this_query LIMIT 100")
    List<Recipe> sortedSuggestions(String this_query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Recipe[] recipes);

}
